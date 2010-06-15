package pcp.algorithms;

import static pcp.utils.ArrayUtils.containsSorted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pcp.algorithms.clique.MaxCliqueFinder;
import pcp.common.sorting.NodeDegreeCompleteComparator;
import pcp.entities.ISimpleGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraphBuilder;

/**
 * Preprocesses the input graph before solving the PCP problem. This includes
 * removing all edges within partitions, isolated nodes with their partitions,
 * and clearing empty partitions.
 */
public class Preprocessor {
	
	private final static boolean log = true;
	
	private PartitionedGraphBuilder builder;
	private int partitionsCheckCount;
	
	private List<pcp.entities.simple.Node> clique;
	
	public Preprocessor(PartitionedGraphBuilder builder) {
		this.builder = builder;
		this.partitionsCheckCount = 0;
	}
	
	private static class PartitionData {
		
		public boolean check;
		
		public PartitionData(boolean check) {
			this.check = check;
		}
	}
	
	public PartitionedGraphBuilder preprocess() {
		// Remove edges in the same partition
		removeEdgesWithinPartition();
		
		// Remove all nodes with siblings whose neighbourhoods can be included
		// as well as all partitions that have nodes with no neighbourhood
		removeRedundantNodes();
		
		// Create a gprime clique and remove nodes based on degree
		// Make sure another check for redundant nodes is made every time
		while (createGPrimeClique() && removeRedundantNodes());
		
		// Return the builder for further processing
		return builder;
	}

	public List<pcp.entities.simple.Node> getClique() {
		return clique;
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
				if (builder.hasPartition(p.index())) {
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

	/**
	 * Generates a gprime clique
	 * @return whether a bigger clique was generated
	 */
	private boolean createGPrimeClique() {
		ISimpleGraph gprime = builder.getGPrime();
		MaxCliqueFinder finder = new MaxCliqueFinder(gprime);
		List<pcp.entities.simple.Node> newclique = finder.run().getClique();
		
		if (log) System.out.println(newclique == null ? "No clique found" : "Found clique of size " + newclique.size());
		this.clique = newclique;
		
		return (newclique.size() > 1 && (this.clique == null || newclique.size() > this.clique.size()));
	}


	private boolean removeRedundantNodes() {
		boolean changes = false;
		
		// Setup partitions data
		for (Partition partition : builder.getPartitions()) {
			partition.setData(new PartitionData(true));
			partitionsCheckCount++;
		}
		
		// Iterate while there are changes in a partition
		while (partitionsCheckCount > 0) {
			
			// Go through the whole collection filtering out those not marked for check
			for (Partition partition : builder.getPartitions()) {
				if (partitionsCheckCount == 0) break;
				PartitionData data = getPartitionData(partition);
				if (!data.check) continue;
				
				// Mark this partition as processed
				data.check = false;
				partitionsCheckCount--;
				
				// Process the partition itself
				changes = processPartition(partition) || changes;
			}
		}
		
		// Make sure no node was left behind
		changes = removePartitionsWithIsolatedNodes() || changes;
		
		return changes;
	}

	private boolean shouldRemoveNodeOnDegree(Node node) {
		// TODO: Check bound
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
				if (log) System.out.println("Removing partition " + partition);
				builder.removePartition(partition);
				break;
			}
			
			// Otherwise look for all previous sets created, if they can
			// be included in current neighbourhood
			boolean removed = false;
			Node[] neighbours = builder.getNeighbours(node);

			for (Node[] existing : neighbourhoods) {
				if (containsSorted(neighbours, existing)) {
					markNeighbourPartitionsForCheck(neighbours);
					if (log) System.out.println("Removing node " + node);
					builder.removeNode(node);
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
		
		return changes;
	}
	
	private PartitionData getPartitionData(Partition p) {
		return (PartitionData) p.getData();
	}
	
	private void markNeighbourPartitionsForCheck(Node[] neighbours) {
		for (int i = 0; i < neighbours.length; i++) {
			Node node = neighbours[i];
			PartitionData data = getPartitionData(builder.getPartition(node)); 
			if (!data.check) {
				data.check = true;
				partitionsCheckCount++;
			}
		}
		
	}
	
	private boolean removePartitionsWithIsolatedNodes() {
		boolean changes = false;
		for (Node node : builder.getNodes()) {
			if (builder.hasNode(node.index())) {
				if (builder.getNeighbours(node).length == 0) {
					if (log) System.out.println("Removing partition " + node.getPartition());
					builder.removePartition(node.getPartition());
					changes = true;
				}
			}
		} return changes;
	}
	
	private void removeEdgesWithinPartition() {
		for (Edge edge : builder.getEdges()) {
			if (edge.getNode1().getPartition().index() == edge.getNode2().getPartition().index()) {
				if (log) System.out.println("Removing edge " + edge);
				builder.removeEdge(edge);
			}
		}
	}
	
}
