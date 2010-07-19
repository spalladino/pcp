package pcp.entities.partitioned;

import pcp.entities.IPartitionedGraph;


public class Partition implements Comparable<Partition> {

	public final int index;
	private final IPartitionedGraph graph;
	
	Partition(IPartitionedGraph graph, int name) {
		this.graph = graph;
		this.index = name;
	}

	@Override
	public int hashCode() {
		return index;
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
		if (index != other.index) return false;
		return true;
	}

	@Override
	public int compareTo(Partition o) {
		return index - o.index;
	}

	@Override
	public String toString() {
		return "P" + this.index;
	}

}
