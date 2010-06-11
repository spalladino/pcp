package pcp.entities.simple;

import java.util.Arrays;
import java.util.Comparator;

import pcp.entities.ISimpleGraph;

public class SortedSimpleGraph implements ISimpleGraph {

	ISimpleGraph graph;
	
	Comparator<Node> nodeComparer;
	Comparator<Edge> edgeComparer;
	
	Node[][] adjacencies;
	
	Node[] nodes;
	Edge[] edges;
	
	public SortedSimpleGraph(ISimpleGraph graph,
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
			this.edges = graph.getEdges().clone();
			Arrays.sort(this.edges, this.edgeComparer);
		} else {
			this.edges = graph.getEdges();
		}
		
		if (this.nodeComparer != null) {
			this.nodes = graph.getNodes().clone();
			Arrays.sort(this.nodes, this.nodeComparer);
		} else {
			this.nodes = graph.getNodes();
		}
		
		this.adjacencies = new Node[graph.N()][];
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
			if (this.nodeComparer != null) {
				this.adjacencies[node.name] = graph.getNeighbours(node).clone();
				Arrays.sort(this.adjacencies[node.name], this.nodeComparer);
			} else {
				this.adjacencies[node.name] = graph.getNeighbours(node);
			}
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
