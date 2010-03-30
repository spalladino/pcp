package pcp.entities.simple;

import pcp.entities.ISimpleGraph;


public class Node implements Comparable<Node> {
	
	int name;
	ISimpleGraph graph;
	
	public int index() {
		return name;
	}

	public Node[] getNeighbours() {
		return graph.getNeighbours(this);
	}
	
	public int getDegree() {
		return graph.getDegree(this);
	}
	
	public boolean isAdjacent(Node n) {
		return graph.areAdjacent(this, n);
	}

	Node(ISimpleGraph graph, int name) {
		this.name = name;
		this.graph = graph;
	}

	@Override
	public int hashCode() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof Node) {
			return false;
		} 
		Node other = (Node) obj;
		return (name == other.name);
	}

	@Override
	public int compareTo(Node o) {
		return ((Integer)name).compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return "X" + this.index();
	}
	
	
}
