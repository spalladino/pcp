package pcp.entities.partitioned;

import pcp.entities.IPartitionedGraph;


public class Node implements Comparable<Node> {
	
	public final int index;
	IPartitionedGraph graph;
	
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
		this.index = name;
		this.graph = graph;
	}

	@Override
	public int hashCode() {
		return index;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof Node) {
			return false;
		} 
		Node other = (Node) obj;
		return (index == other.index);
	}

	@Override
	public int compareTo(Node o) {
		return index - o.index;
	}
	
	@Override
	public String toString() {
		return "X" + this.index + "(" + this.getPartition().index + ")";
	}
	
	
}
