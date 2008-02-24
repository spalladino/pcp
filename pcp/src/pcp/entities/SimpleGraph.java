package pcp.entities;

import pcp.interfaces.ISimpleGraph;

public class SimpleGraph implements ISimpleGraph {

	boolean[][] matrix;

	SimpleNode[][] adjacencies;
	SimpleNode[] nodes;
	SimpleEdge[] edges;
	
	String name;
	
	SimpleGraph() { }
	
	SimpleGraph(int nodescount, int edgecount) {
		this.edges = new SimpleEdge[edgecount];
		this.nodes = new SimpleNode[nodescount];
		this.matrix = new boolean[nodescount][nodescount];
		this.adjacencies = new SimpleNode[nodescount][];
	}
	
	@Override
	public SimpleNode[] getNodes() {
		return nodes;
	}
	
	@Override
	public SimpleEdge[] getEdges() {
		return edges;
	}
	
	@Override
	public boolean areAdjacent(SimpleNode n1, SimpleNode n2) {
		return matrix[n1.name][n2.name];
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SimpleNode[] getNeighbours(SimpleNode node) {
		return this.adjacencies[node.name];
	}

	@Override
	public int getDegree(SimpleNode node) {
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
	public SimpleNode getNode(int index) {
		return this.nodes[index];
	}
	
}
