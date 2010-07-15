package pcp.entities.partitioned;

import java.io.PrintStream;

import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Graph;
import pcp.utils.GraphUtils;


public class PartitionedGraph implements IPartitionedGraph  {

	// Index i0, i1 contains whether nodes i0 and i1 are adjacent 
	boolean[][] matrix;

	// Index i contains the neighbours of node i
	Node[][] adjacencies;
	
	// Index k contains the nodes that belong to partition k
	Node[][] partitionNodes;
	
	// Index i contains the partition node i belongs to
	Partition[] nodePartition;
	
	// Index i contains array of partitions adjacent to node i
	Partition[][] nodePartitionAdjacencies;

	// Index k contains array of partitions adjacent to partition k
	Partition[][] partitionPartitionAdjacencies;
	
	// Index k contains array of nodes adjacent to partition k
	Node[][] partitionNodeAdjacencies;
	
	// G' graph corresponding to this partitioned graph
	Graph gprime;
	
	final Node[] nodes;
	final Partition[] partitions;
	final Edge[] edges;
	
	String name;
	
	PartitionedGraph(Node[] nodes, Edge[] edges, Partition[] partitions) {
		this.nodes = nodes;
		this.partitions = partitions;
		this.edges = edges;
	}
	
	PartitionedGraph(int nodescount, int edgecount, int partitionscount) {
		this.edges = new Edge[edgecount];
		this.nodes = new Node[nodescount];
		this.partitions = new Partition[partitionscount];
		this.matrix = new boolean[nodescount][nodescount];
		this.adjacencies = new Node[nodescount][];
		this.nodePartitionAdjacencies = new Partition[nodescount][];
		this.partitionNodes = new Node[partitionscount][];
		this.nodePartition = new Partition[nodescount];
		this.partitionPartitionAdjacencies = new Partition[partitionscount][];
		this.partitionNodeAdjacencies = new Node[partitionscount][];
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
		return matrix[n1.index][n2.index];
	}

	public String getName() {
		return name;
	}

	public Node[] getNodes(Partition partition) {
		return partitionNodes[partition.name];
	}

	public Node[] getNeighbours(Node node) {
		return this.adjacencies[node.index];
	}

	public Partition getPartition(Node node) {
		return this.nodePartition[node.index];
	}

	public int getDegree(Node node) {
		return this.adjacencies[node.index].length;
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
		return this.nodePartition[n1.index].name == this.nodePartition[n2.index].name;
	}

	@Override
	public Partition[] getNeighbourPartitions(Node n) {
		return this.nodePartitionAdjacencies[n.index];
	}

	@Override
	public Partition[] getNeighbourPartitions(Partition p) {
		return this.partitionPartitionAdjacencies[p.index()];
	}

	@Override
	public Node[] getNeighbours(Partition p) {
		return this.partitionNodeAdjacencies[p.index()];
	}

	@Override
	public ISimpleGraph getGPrime() {
		return this.gprime;
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return matrix[n1][n2];
	}
	
	@Override
	public Node[] getNodes(pcp.entities.simple.Node simpleNode) {
		return this.getNodes(partitions[simpleNode.index()]);
	}
	
	public void print(PrintStream stream) {
		GraphUtils.print(this, stream);
	}

	@Override
	public Partition getPartition(int partition) {
		return partitions[partition];
	}
}
