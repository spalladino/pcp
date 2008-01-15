package pcp.entities;

import pcp.interfaces.IPartitionedGraph;


public class PartitionedGraph implements IPartitionedGraph  {

	boolean[][] matrix;

	Node[][] adjacencies;
	Node[][] partitionNodes;
	Partition[] nodePartition;
	
	Node[] nodes;
	Partition[] partitions;
	Edge[] edges;
	
	String name;
	
	PartitionedGraph() { }
	
	PartitionedGraph(int nodescount, int edgecount, int partitionscount) {
		this.edges = new Edge[edgecount];
		this.nodes = new Node[nodescount];
		this.partitions = new Partition[partitionscount];
		this.matrix = new boolean[nodescount][nodescount];
		this.adjacencies = new Node[nodescount][];
		this.partitionNodes = new Node[partitionscount][];
		this.nodePartition = new Partition[nodescount];
	}
	
	public Node[] getNodes() {
		return nodes;
	}
	
	public Partition[] getPartitions() {
		return partitions;
	}
	
	public Edge[] getEdges() {
		return edges;
	}
	
	public boolean areAdjacent(Node n1, Node n2) {
		return matrix[n1.name][n2.name];
	}

	public String getName() {
		return name;
	}

	public Node[] getNodes(Partition partition) {
		return partitionNodes[partition.name];
	}

	public Node[] getNeighbours(Node node) {
		return this.adjacencies[node.name];
	}

	public Partition getPartition(Node node) {
		return this.nodePartition[node.name];
	}

	public int getDegree(Node node) {
		return this.adjacencies[node.name].length;
	}

	@Override
	public int P() {
		return this.partitions.length;
	}

	@Override
	public int E() {
		return this.edges.length;
	}

	@Override
	public int N() {
		return this.nodes.length;
	}

	@Override
	public Node getNode(int index) {
		return this.nodes[index];
	}

	@Override
	public boolean areInSamePartition(Node n1, Node n2) {
		return this.nodePartition[n1.name].name == this.nodePartition[n2.name].name;
	}
}
