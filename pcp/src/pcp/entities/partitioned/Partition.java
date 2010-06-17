package pcp.entities.partitioned;

import pcp.entities.IPartitionedGraph;


public class Partition implements Comparable<Partition> {

	int name;
	IPartitionedGraph graph;
	
	public int index() {
		return name;
	}
	
	Partition(IPartitionedGraph graph, int name) {
		this.graph = graph;
		this.name = name;
	}

	@Override
	public int hashCode() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Partition other = (Partition) obj;
		if (graph == null) {
			if (other.graph != null) return false;
		} else if (!graph.equals(other.graph)) return false;
		if (name != other.name) return false;
		return true;
	}

	@Override
	public int compareTo(Partition o) {
		return ((Integer)name).compareTo(o.name);
	}

	@Override
	public String toString() {
		return "P" + this.index();
	}

}
