package pcp.entities.partitioned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pcp.entities.IPartitionedGraph;
import pcp.entities.IPartitionedGraphBuilder;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Graph;
import pcp.utils.GraphUtils;

public class PartitionedGraphBuilder implements IPartitionedGraph, IPartitionedGraphBuilder {
	
	String name;
	
	List<Edge> edges;
	
	Map<Integer, Node> nodes;
	Map<Integer, Partition> partitions;
	
	Map<Partition, Set<Node>> partitionNodes;
	Map<Node, Partition> nodePartition;
	Map<Node, Set<Node>> nodeAdjacencies;
	
	boolean mustRecreate = false;
	
	public PartitionedGraphBuilder(String name) {
		super();
		this.name = name;

		initialize();
	}

	public PartitionedGraph getGraph() {
		if (mustRecreate) recreateGraph();
		
		int nn = nodes.size();
		int pp = partitions.size();
		int ee = edges.size();
		
		PartitionedGraph graph = new PartitionedGraph(nn, ee, pp);
		graph.name = this.name;

		Edge[] builderEdges = getEdges();
		
		for (int i = 0; i < nn; i++)  graph.getNodes()[i] = new Node(graph, i);
		for (int i = 0; i < pp; i++)  graph.getPartitions()[i] = new Partition(graph, i);
		for (int i = 0; i < ee; i++)  graph.getEdges()[i] = new Edge(graph.getNodes()[builderEdges[i].index1], graph.getNodes()[builderEdges[i].index2]);
		
		for (Node node : graph.nodes) {
			graph.getNodePartition()[node.index] = graph.getPartitions()[this.getPartition(node).index];
			graph.getAdjacencies()[node.index] = toGraph(this.getNeighbours(node), graph);
			graph.getNodePartitionAdjacencies()[node.index] = toGraph(this.getNeighbourPartitions(node), graph);
		}
		
		for (Partition partition : graph.partitions) {
			graph.getPartitionNodes()[partition.index] = toGraph(this.getNodes(partition), graph);
			graph.getPartitionNodeAdjacencies()[partition.index] = toGraph(this.getNeighbours(partition), graph);
			graph.getPartitionPartitionAdjacencies()[partition.index] = toGraph(this.getNeighbourPartitions(partition), graph);
		}
		
		for (Edge edge : graph.edges) {
			graph.getMatrix()[edge.index1][edge.index2] = true;
			graph.getMatrix()[edge.index2][edge.index1] = true;
		}
		
		graph.gprime = (Graph)getGPrime();
		
		return graph;
	}
	
	private Partition[] toGraph(Partition[] partitions, PartitionedGraph graph) {
		Partition[] gpartitions = new Partition[partitions.length];
		for (int i = 0; i < partitions.length; i++) {
			gpartitions[i] = graph.getPartitions()[partitions[i].index];
		} return gpartitions;
	}
	
	private Node[] toGraph(Node[] nodes, PartitionedGraph graph) {
		Node[] gnodes = new Node[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			gnodes[i] = graph.getNodes()[nodes[i].index];
		} return nodes;
	}

	private void initialize() {
		this.partitionNodes = new TreeMap<Partition, Set<Node>>();
		this.partitions = new TreeMap<Integer, Partition>();
		this.nodeAdjacencies = new TreeMap<Node, Set<Node>>();
		this.nodes = new TreeMap<Integer, Node>();
		this.edges = new ArrayList<Edge>();
		this.nodePartition = new TreeMap<Node, Partition>();
	}

	private Node getCreateNode(int node) {
		if (!nodes.containsKey(node)) {
			Node n = new Node(this, node);
			nodes.put(node, n);
			nodeAdjacencies.put(n, new HashSet<Node>());
		} return nodes.get(node);
	}
	
	private Partition getCreatePartition(int partition) {
		if (!partitions.containsKey(partition)) {
			Partition p = new Partition(this, partition);
			partitions.put(partition, p);
			partitionNodes.put(p, new HashSet<Node>());
		} return partitions.get(partition);
	}
	
	public PartitionedGraphTranslator recreateGraph() {
		return recreateGraph(new ArrayList<Partition>(0));
	}
	
	public PartitionedGraphTranslator recreateGraph(List<Partition> firstPartitions) {
		Map<Integer,Integer> oldToNewNode = new HashMap<Integer, Integer>();
		Map<Integer,Integer> oldToNewPartition = new HashMap<Integer, Integer>();
		
		Node[] freshNodes = this.getNodes().clone();
		for (int i = 0; i < freshNodes.length; i++) {
			Node node = freshNodes[i];
			oldToNewNode.put(node.index, i);
		}
	
		Partition[] freshPartitions = this.getPartitions().clone();
		int partitionNewIndex = 0;
		
		for (Partition partition : firstPartitions) {
			oldToNewPartition.put(partition.index, partitionNewIndex);
			partitionNewIndex++;
		}
		
		for (Partition partition : freshPartitions) {
			if (!firstPartitions.contains(partition)) {
				oldToNewPartition.put(partition.index, partitionNewIndex);
				partitionNewIndex++;
			}
		}
		
		Map<Node, Partition> nodePartition = this.nodePartition;
		List<Edge> edges = this.edges;
		
		initialize();
	
		for (Node node : nodePartition.keySet()) {
			int newkey = oldToNewNode.get(node.index);
			int newpart = oldToNewPartition.get(nodePartition.get(node).index);
			this.addNode(newkey, newpart);
		}
		
		for (Edge edge : edges) {
			int n1 = oldToNewNode.get(edge.index1);
			int n2 = oldToNewNode.get(edge.index2);
			this.addEdge(n1, n2);
		}
		
		mustRecreate = false;
		
		return new PartitionedGraphTranslator(oldToNewNode, oldToNewPartition);
	}

	public void removePartition(Partition partition) {
		Set<Node> set = partitionNodes.get(partition);
		Node[] nodes = (Node[]) set.toArray(new Node[set.size()]);
		for (Node node : nodes) {
			removeNode(node);
		}
		partitionNodes.remove(partition);
		partitions.remove(partition.index);
		mustRecreate = true;
	}
	
	public void removeNode(Node node) {
		// Remove from containing partition
		Partition p = this.getPartition(node);
		partitionNodes.get(p).remove(node);
		
		// Remove adjacent edges
		List<Edge> remove = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (edge.node1.equals(node) || edge.node2.equals(node)) {
				remove.add(edge);
			}
		} edges.removeAll(remove);
		
		// Remove adjacencies data
		for (Node other : nodeAdjacencies.keySet()) {
			if (!other.equals(node)) {
				nodeAdjacencies.get(other).remove(node);
			}
		}

		// Remove from node indexed collections
		nodePartition.remove(node);
		nodeAdjacencies.remove(node);
		nodes.remove(node.index);
		
		mustRecreate = true;
	}
	
	public void removeEdge(Edge edge) {
		edges.remove(edge);
		Node n1 = edge.node1;
		Node n2 = edge.node2;
		nodeAdjacencies.get(n1).remove(n2);
		nodeAdjacencies.get(n2).remove(n1);
		mustRecreate = true;
	}

	public PartitionedGraphBuilder createNodes(int count) {
		createNodes(0, count);
		return this;
	}
	
	public void createNodes(int from, int count) {
		for (int i = from; i < from + count; i++) {
			addNode(i, i);
		}
	}
	
	public PartitionedGraphBuilder addNode(int node, int partition) {
		Node n = getCreateNode(node);
		Partition p = getCreatePartition(partition);
		partitionNodes.get(p).add(n);
		nodePartition.put(n, p);
		
		return this;
	}
	
	public PartitionedGraphBuilder addNodesInPartition(int partition, int... nodes) {
		for (int node : nodes)
			addNode(node, partition);
		return this;
	}
	
	public PartitionedGraphBuilder addEdges(int from, int... to) {
		for (int n : to) {
			addEdge(from, n);
		}
		return this;
	}
	
	public PartitionedGraphBuilder addEdge(int n1, int n2) {
		Node node1 = getCreateNode(n1);
		Node node2 = getCreateNode(n2);
		
		// Do not add repeated edges
		if (this.nodeAdjacencies.get(node1).contains(node2) ||
			this.nodeAdjacencies.get(node2).contains(node1)) {
			return this;
		}
		
		edges.add(new Edge(node1, node2));
		
		nodeAdjacencies.get(node1).add(node2);
		nodeAdjacencies.get(node2).add(node1);
		
		return this;
	}
	
	public boolean hasNode(int val) {
		return this.nodes.containsKey(val);
	}
	
	public boolean hasPartition(int val) {
		return this.partitions.containsKey(val);
	}

	@Override
	public Node[] getNodes(Partition partition) {
		Set<Node> nodes = partitionNodes.get(partition);
		return (Node[]) nodes.toArray(new Node[nodes.size()]);
	}

	@Override
	public Partition getPartition(Node node) {
		return nodePartition.get(node);
	}

	@Override
	public Partition[] getPartitions() {
		Collection<Partition> values = partitions.values();
		return (Partition[]) values.toArray(new Partition[values.size()]);
	}

	@Override
	public boolean areAdjacent(Node n1, Node n2) {
		return nodeAdjacencies.get(n1).contains(n2);
	}

	@Override
	public boolean areInSamePartition(Node n1, Node n2) {
		return nodePartition.get(n1).index == nodePartition.get(n2).index;
	}

	@Override
	public Edge[] getEdges() {
		return (Edge[]) edges.toArray(new Edge[edges.size()]);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Node[] getNeighbours(Node node) {
		Set<Node> set = nodeAdjacencies.get(node);
		return (Node[]) set.toArray(new Node[set.size()]);
	}

	@Override
	public Node[] getNodes() {
		Collection<Node> values = nodes.values();
		return (Node[]) values.toArray(new Node[values.size()]);
	}

	@Override
	public int getDegree(Node node) {
		return this.nodeAdjacencies.get(node).size();
	}

	@Override
	public int P() {
		return this.partitions.size();
	}

	@Override
	public int E() {
		return this.edges.size();
	}

	@Override
	public int N() {
		return this.nodes.size();
	}

	@Override
	public Node getNode(int index) {
		return this.nodes.get(index);
	}

	@Override
	public Partition[] getNeighbourPartitions(Node n) {
		Set<Partition> partitions = GraphUtils.groupByPartition(this.getNeighbours(n)).keySet();
		return (Partition[]) partitions.toArray(new Partition[partitions.size()]);
	}

	@Override
	public Partition[] getNeighbourPartitions(Partition p) {
		Set<Partition> partitions = GraphUtils.groupByPartition(this.getNeighbours(p)).keySet();
		return (Partition[]) partitions.toArray(new Partition[partitions.size()]);
	}

	@Override
	public Node[] getNeighbours(Partition p) {
		Set<Node> nodes = new HashSet<Node>();
		for (Node n : getNodes(p)) {
			for (Node neighbour : this.getNeighbours(n)) {
				nodes.add(neighbour);
			}
		} return (Node[]) nodes.toArray(new Node[nodes.size()]);
	}

	@Override
	public ISimpleGraph getGPrime() {
		return new GPrimeBuilder(this).getGraph();
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return areAdjacent(nodes.get(n1), nodes.get(n2));
	}

	@Override
	public Node[] getNodes(pcp.entities.simple.Node simpleNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Partition getPartition(int partition) {
		return getPartitions()[partition];
	}

	@Override
	public String toString() {
		return String.format("%1$s (%2$d,%3$d,%4$d)", this.name, this.N(), this.E(), this.P());
	}
	
}
