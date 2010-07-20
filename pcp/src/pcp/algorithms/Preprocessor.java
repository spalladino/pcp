package pcp.algorithms;

import static pcp.utils.ArrayUtils.containsSorted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pcp.algorithms.bounding.TimeBounder;
import pcp.algorithms.clique.MaxCliqueFinder;
import pcp.common.sorting.NodeDegreeCompleteComparator;
import pcp.entities.ISimpleGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.entities.partitioned.PartitionedGraphTranslator;
import pcp.interfaces.IExecutionDataProvider;
import pcp.utils.GraphUtils;
import props.Settings;

/**
 * Preprocesses the input graph before solving the PCP problem. This includes
 * removing all edges within partitions, isolated nodes with their partitions,
 * and clearing empty partitions.
 */
public class Preprocessor implements IExecutionDataProvider {
	
	private final static boolean cliqueEnabled = Settings.get().getBoolean("clique.gprime.initial.enabled");
	private final static boolean log = Settings.get().getBoolean("logging.preprocess");
	
	private PartitionedGraphBuilder builder;
	private List<pcp.entities.simple.Node> clique;
	
	private int maxcliquesize = 0;
	private int partitionsRemoved = 0;
	private int edgesRemoved = 0;
	private int nodesRemoved = 0;
	
	private TimeBounder bounder;
	
	public Preprocessor(PartitionedGraphBuilder builder) {
		this.builder = builder;
		this.bounder = new TimeBounder();
	}
	
	public PartitionedGraphBuilder preprocess() {
		bounder.start();
		
		// Remove edges in the same partition
		removeEdgesWithinPartition();
		
		// Remove all nodes with siblings whose neighbourhoods can be included
		// as well as all partitions that have nodes with no neighbourhood
		removeRedundantNodes();
		
		// Create a gprime clique and remove nodes based on degree
		// Make sure another check for redundant nodes is made every time
		while (cliqueEnabled && recreateGraph() && createGPrimeClique() && removeRedundantNodes());
		
		// Rename the graph's nodes and partitions to ensure that the first nodes are the one in the clique
		renameClique();
		
		bounder.stop();
		return builder;
	}

	private void renameClique() {
		if (clique != null) {
			PartitionedGraphTranslator translator = builder.recreateGraph(GraphUtils.simpleNodesToPartitions(builder, clique));
			List<pcp.entities.simple.Node> newclique = new ArrayList<pcp.entities.simple.Node>(clique.size());
			for (pcp.entities.simple.Node old : clique) {
				Integer newindex = translator.translatePartition(old.index());
				newclique.add(builder.getGPrime().getNode(newindex));
			} this.clique = newclique;
		}
	}

	private boolean recreateGraph() {
		builder.recreateGraph();
		return true;
	}

	public List<pcp.entities.simple.Node> getClique() {
		return clique;
	}
	
	@Override
	public void fillData(Map<String, Object> data) {
		data.put("preprocess.time", bounder.getMillis());
		data.put("preprocess.edgesremoved", edgesRemoved);
		data.put("preprocess.nodesremoved", nodesRemoved);
		data.put("preprocess.partitionsremoved", partitionsRemoved);
		data.put("preprocess.cliquesize", clique == null ? 0 : clique.size());
		data.put("preprocess.maxcliquesize", maxcliquesize);
	}

	/**
	 * Generates a gprime clique
	 * @return whether a bigger clique was generated
	 */
	private boolean createGPrimeClique() {
		ISimpleGraph gprime = builder.getGPrime();
		MaxCliqueFinder finder = new MaxCliqueFinder(gprime);
		List<pcp.entities.simple.Node> newclique = finder.run().getClique();
		
		if (log) System.out.println(newclique == null ? "No clique found" : "Found clique of size " + newclique.size());
		boolean retval = (newclique != null && newclique.size() > 1 && (this.clique == null || newclique.size() > this.clique.size()));
		
		this.clique = newclique;
		
		if (clique != null && this.maxcliquesize < this.clique.size()) 
			this.maxcliquesize = this.clique.size();
		
		return retval;
	}


	private boolean removeRedundantNodes() {
		boolean changes = true;
		boolean retval = false;
		
		// Iterate while there are changes in a partition
		while (changes) {
			changes = false;
			for (Partition partition : builder.getPartitions()) {
				changes = processPartition(partition) || changes;
				retval = changes || retval;
			}
		}
		
		return retval;
	}

	private boolean shouldRemoveNodeOnDegree(Node node) {
		return builder.getDegree(node) == 0
			|| (clique != null && builder.getNeighbourPartitions(node).length < clique.size());
	}
	
	private boolean processPartition(Partition partition) {
		// Setup structures
		Node[] sorted = builder.getNodes(partition);
		Arrays.sort(sorted, new NodeDegreeCompleteComparator());
		List<Node[]> neighbourhoods = new ArrayList<Node[]>(sorted.length);
		boolean changes = false;
		
		// Iterate through nodes in degree ascending order
		nodes: for (int n = 0; n < sorted.length; n++) {
			Node node = sorted[n];
			
			// If there is an isolated node or its neighbours are lower than the clique number, remove this partition
			if (shouldRemoveNodeOnDegree(node)) {
				changes = true;
				removePartition(partition);
				break;
			}
			
			// Otherwise look for all previous sets created, if they can
			// be included in current neighbourhood
			boolean removed = false;
			Node[] neighbours = builder.getNeighbours(node);

			for (Node[] existing : neighbourhoods) {
				if (containsSorted(neighbours, existing)) {
					removeNode(node);
					changes = true;
					removed = true;
					break nodes;
				}
			}
			
			// If we did not find a sibling that is included within the
			// current one, then add it to the set
			if (!removed) { 
				neighbourhoods.add(neighbours);
			}
		}
		
		// Remove partition if no nodes were left
		if (builder.hasPartition(partition.index) && builder.getNodes(partition).length == 0) {
			removePartition(partition);
			changes = true;
		}
		
		return changes;
	}

	private void removePartition(Partition partition) {
		if (log) System.out.println("Removing partition " + partition);
		partitionsRemoved++;
		nodesRemoved+= builder.getNodes(partition).length;
		builder.removePartition(partition);
	}
	
	private void removeNode(Node node) {
		if (log) System.out.println("Removing node " + node);
		nodesRemoved++;
		builder.removeNode(node);
	}
	
	private void removeEdgesWithinPartition() {
		for (Edge edge : builder.getEdges()) {
			if (edge.node1.getPartition().index == edge.node2.getPartition().index) {
				if (log) System.out.println("Removing edge " + edge);
				builder.removeEdge(edge);
				edgesRemoved++;
			}
		}
	}

	/**
	 * Removes all nodes with partition degree less than clique number minus one.
	 * When a node is removed all its partition is removed from the graph.
	 * @return whether there was any node removed
	 * @deprecated use remove redundant nodes method which includes this functionality
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private boolean removeNodesOnDegree() {
		boolean ret = false;
		boolean changes = true;
		
		// Iterate while there are changes on the graph
		while (changes) {
			changes = false;
			// Check every node in the partition to see if it can be removed
			for (Partition p : builder.getPartitions()) {
				if (builder.hasPartition(p.index)) {
					for (Node node : builder.getNodes(p)) {
						if (builder.getNeighbourPartitions(node).length < clique.size() - 1) {
							builder.removePartition(node.getPartition());
							ret = changes = true;
							break;
						}
					}
				}
			}
		} return ret;
	}

	@Deprecated
	@SuppressWarnings("unused")
	private boolean removePartitionsWithIsolatedNodes() {
		boolean changes = false;
		for (Node node : builder.getNodes()) {
			if (builder.hasNode(node.index)) {
				if (builder.getNeighbours(node).length == 0) {
					removePartition(node.getPartition());
					changes = true;
				}
			}
		} return changes;
	}
	
}
