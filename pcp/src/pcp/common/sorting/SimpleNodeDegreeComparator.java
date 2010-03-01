package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.SimpleNode;

public class SimpleNodeDegreeComparator implements Comparator<SimpleNode> {
	
	boolean reverse = false;
	
	public SimpleNodeDegreeComparator() {
	}
	
	public SimpleNodeDegreeComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(SimpleNode n1, SimpleNode n2) {
		if (reverse) return n2.getDegree() - n1.getDegree();
		else return n1.getDegree() - n2.getDegree();
	}
}
