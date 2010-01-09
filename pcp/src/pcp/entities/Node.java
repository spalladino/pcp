package pcp.entities;

import pcp.interfaces.IPartitionedGraph;


public class Node implements Comparable<Node> {
	
	int name;
	IPartitionedGraph graph;
	
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

	public boolean isAdjacentOrSamePartition(Node n) {
		return (graph.areAdjacent(this, n)) || (getPartition() == n.getPartition());
	}
	
	public Partition getPartition() {
		return graph.getPartition(this);
	}
	
	Node(IPartitionedGraph graph, int name) {
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
