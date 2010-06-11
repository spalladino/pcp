package pcp.entities.simple;

import java.util.Arrays;
import java.util.Comparator;

import pcp.entities.ISimpleGraph;

public class SortedSimpleGraph implements ISimpleGraph {

	Graph graph;
	
	Comparator<Node> nodeComparer;
	Comparator<Edge> edgeComparer;
	
	Node[][] adjacencies;
	
	Node[] nodes;
	Edge[] edges;
	
	public SortedSimpleGraph(Graph graph,
			Comparator<Node> nodeComparer,
			Comparator<Edge> edgeComparer) {
		
		super();
		
		this.graph = graph;
		
		this.edgeComparer = edgeComparer;
		this.nodeComparer = nodeComparer;
		
		reset();
	}

	public void reset() {
		if (this.edgeComparer != null) {
			this.edges = graph.edges.clone();
			Arrays.sort(this.edges, this.edgeComparer);
		} else {
			this.edges = graph.edges;
		}
		
		if (this.nodeComparer != null) {
			this.nodes = graph.nodes.clone();
			Arrays.sort(this.nodes, this.nodeComparer);
		} else {
			this.nodes = graph.nodes;
		}
		
		if (this.nodeComparer != null) {
			this.adjacencies = new Node[graph.N()][];
		} else {
			this.adjacencies = graph.adjacencies;
		} 
	}
	
	@Override
	public Node[] getNodes() {
		return this.nodes;
	}

	@Override
	public Edge[] getEdges() {
		return this.edges;
	}


	@Override
	public Node[] getNeighbours(Node node) {
		if (this.adjacencies[node.name] == null) {
			this.adjacencies[node.name] = graph.adjacencies[node.name].clone();
			Arrays.sort(this.adjacencies[node.name], this.nodeComparer);
		}
		return this.adjacencies[node.name];
	}

	@Override
	public Node getNode(int index) {
		return graph.getNode(index);
	}

	@Override
	public int getDegree(Node node) {
		return graph.getDegree(node);
	}

	@Override
	public boolean areAdjacent(Node n1, Node n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public int N() {
		return graph.N();
	}

	@Override
	public int E() {
		return graph.E();
	}

	@Override
	public String getName() {
		return graph.getName();
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return graph.areAdjacent(n1, n2);
	}
	
}
