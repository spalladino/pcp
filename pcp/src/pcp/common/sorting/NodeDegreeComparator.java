package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.partitioned.Node;

public class NodeDegreeComparator implements Comparator<Node> {
	
	boolean reverse = false;
	
	public NodeDegreeComparator() {
	}
	
	public NodeDegreeComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(Node n1, Node n2) {
		if (reverse) return n2.getDegree() - n1.getDegree();
		else return n1.getDegree() - n2.getDegree();
	}
}
