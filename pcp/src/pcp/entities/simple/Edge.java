package pcp.entities.simple;


public class Edge {
	Node node1;
	Node node2;
	
	public Node getNode1() {
		return node1;
	}
	
	public Node getNode2() {
		return node2;
	}
	
	public int index1() {
		return node1.name;
	}
	
	public int index2() {
		return node2.name;
	}

	Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
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
		
		return (this.node1.equals(other.node1) && this.node2.equals(other.node2))
			|| (this.node2.equals(other.node1) && this.node1.equals(other.node2));
	}
	
	@Override
	public String toString() {
		return String.format("E(%1$s,%2$s)", node1.index(), node2.index());
	}
	
	
}
