package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.simple.Node;

public class SimpleNodeIndexComparator implements Comparator<Node> {
	
	boolean reverse = false;
	
	public SimpleNodeIndexComparator() {
	}
	
	public SimpleNodeIndexComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(Node n1, Node n2) {
		int val = n1.index() - n2.index();
		return reverse ? -val : val;
	}
}
