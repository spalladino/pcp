package pcp.entities.partitioned;


public class Edge {
	public final Node node1;
	public final Node node2;
	
	public final int index1;
	public final int index2;
	
	Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
		this.index1 = node1.index;
		this.index2 = node2.index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
		result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Edge other = (Edge) obj;
		
		return (this.node1.index == other.node1.index && this.node2.index == other.node2.index)
			|| (this.node2.index == other.node1.index && this.node1.index == other.node2.index);
	}
	
	@Override
	public String toString() {
		return String.format("E(%1$s,%2$s)", node1.index, node2.index);
	}
	
	
}
