package pcp.entities.simple;

import java.io.PrintStream;
import java.util.Arrays;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Node;

public class Graph implements ISimpleGraph {

	boolean[][] matrix;

	Node[][] adjacencies;
	Node[] nodes;
	Edge[] edges;
	
	String name;
	
	Graph() { }
	
	Graph(int nodescount, int edgecount) {
		this.edges = new Edge[edgecount];
		this.nodes = new Node[nodescount];
		this.matrix = new boolean[nodescount][nodescount];
		this.adjacencies = new Node[nodescount][];
	}
	
	@Override
	public Node[] getNodes() {
		return nodes;
	}
	
	@Override
	public Edge[] getEdges() {
		return edges;
	}
	
	@Override
	public boolean areAdjacent(Node n1, Node n2) {
		return matrix[n1.name][n2.name];
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Node[] getNeighbours(Node node) {
		return this.adjacencies[node.name];
	}

	@Override
	public int getDegree(Node node) {
		return this.adjacencies[node.name].length;
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
	public boolean areAdjacent(int n1, int n2) {
		return matrix[n1][n2];
	}
	
	public void print(PrintStream stream) {
		stream.println("|N|= " + N());
		for (Node n : getNodes()) {
			stream.println(n.toString() + ": " + Arrays.toString(getNeighbours(n)));
		}
	}
	
}
