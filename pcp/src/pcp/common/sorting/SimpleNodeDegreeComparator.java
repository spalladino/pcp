package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.simple.Node;

public class SimpleNodeDegreeComparator implements Comparator<Node> {
	
	boolean reverse = false;
	
	public SimpleNodeDegreeComparator() {
	}
	
	public SimpleNodeDegreeComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(Node n1, Node n2) {
		if (reverse) return n2.getDegree() - n1.getDegree();
		else return n1.getDegree() - n2.getDegree();
	}
}
