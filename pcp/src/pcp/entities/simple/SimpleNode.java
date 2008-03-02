package pcp.entities.simple;

import pcp.entities.ISimpleGraph;


public class SimpleNode implements Comparable<SimpleNode> {
	
	int name;
	ISimpleGraph graph;
	
	public int index() {
		return name;
	}

	public SimpleNode[] getNeighbours() {
		return graph.getNeighbours(this);
	}
	
	public int getDegree() {
		return graph.getDegree(this);
	}
	
	public boolean isAdjacent(SimpleNode n) {
		return graph.areAdjacent(this, n);
	}

	SimpleNode(ISimpleGraph graph, int name) {
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
		if (obj instanceof SimpleNode) {
			return false;
		} 
		SimpleNode other = (SimpleNode) obj;
		return (name == other.name);
	}

	@Override
	public int compareTo(SimpleNode o) {
		return ((Integer)name).compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return "X" + this.index();
	}
	
	
}
