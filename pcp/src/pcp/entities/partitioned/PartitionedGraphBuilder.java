package pcp.entities.partitioned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pcp.entities.IPartitionedGraph;
import pcp.entities.IPartitionedGraphBuilder;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.SimpleGraph;
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

		this.partitionNodes = new TreeMap<Partition, Set<Node>>();
		this.partitions = new TreeMap<Integer, Partition>();
		this.nodeAdjacencies = new TreeMap<Node, Set<Node>>();
		this.nodes = new TreeMap<Integer, Node>();
		this.edges = new ArrayList<Edge>();
		this.nodePartition = new TreeMap<Node, Partition>();
	}
	
	public PartitionedGraph getGraph() {
		if (mustRecreate) recreateGraph();
		PartitionedGraph graph = new PartitionedGraph();
		graph.name = this.name;
		
		int nn = nodes.size();
		int pp = partitions.size();
		
		graph.matrix = new boolean[nn][nn];
		graph.adjacencies = new Node[nn][];
		graph.partitionNodes = new Node[pp][];
		graph.nodePartition = new Partition[nn];
		graph.nodePartitionAdjacencies = new Partition[nn][pp];
		graph.partitionPartitionAdjacencies = new Partition[pp][];
		graph.partitionNodeAdjacencies = new Node[pp][];
		
		graph.nodes = this.getNodes();
		graph.edges = this.getEdges();
		graph.partitions = this.getPartitions();
		
		for (Node node : graph.nodes) {
			node.graph = graph;
			graph.nodePartition[node.name] = this.getPartition(node);
			graph.adjacencies[node.name] = this.getNeighbours(node);
			graph.nodePartitionAdjacencies[node.name] = this.getNeighbourPartitions(node);
		}
		
		for (Partition partition : graph.partitions) {
			partition.graph = graph;
			graph.partitionNodes[partition.name] = this.getNodes(partition);
			graph.partitionNodeAdjacencies[partition.name] = this.getNeighbours(partition);
			graph.partitionPartitionAdjacencies[partition.name] = this.getNeighbourPartitions(partition);
		}
		
		for (Edge edge : graph.edges) {
			graph.matrix[edge.node1.index()][edge.node2.index()] = true;
			graph.matrix[edge.node2.index()][edge.node1.index()] = true;
		}
		
		graph.gprime = (SimpleGraph)getGPrime();
		
		return graph;
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
	
	public void removePartition(Partition partition) {
		Set<Node> set = partitionNodes.get(partition);
		Node[] nodes = (Node[]) set.toArray(new Node[set.size()]);
		for (Node node : nodes) {
			removeNode(node);
		}
		partitionNodes.remove(partition);
		partitions.remove(partition.name);
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
		nodes.remove(node.name);
		
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

	public void recreateGraph() {
		Node[] remainingNodes = this.getNodes();
		nodes.clear();
		for (int i = 0; i < remainingNodes.length; i++) {
			Node node = remainingNodes[i];
			nodes.put(i, node);
			node.name = i;
		}

		List<Partition> remainingPartitions = new ArrayList<Partition>(partitions.size());
		for (Partition partition : partitions.values()) {
			if (!partitionNodes.get(partition).isEmpty()) {
				remainingPartitions.add(partition);
			}
		}
		
		partitions.clear();
		for (int i = 0; i < remainingPartitions.size(); i++) {
			Partition partition = remainingPartitions.get(i);
			partitions.put(i, partition);
			partition.name = i;
		}
		
		mustRecreate = false;
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
		return nodePartition.get(n1).name == nodePartition.get(n2).name;
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
		for (Node n : p.getNodes()) {
			for (Node neighbour : n.getNeighbours()) {
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
	
}