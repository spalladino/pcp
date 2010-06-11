package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.simple.Node;

public class SimpleNodeDegreeCompleteComparator implements Comparator<Node> {
	
	boolean reverse = false;
	
	public SimpleNodeDegreeCompleteComparator() {
	}
	
	public SimpleNodeDegreeCompleteComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(Node n1, Node n2) {
		int val = n1.getDegree() - n2.getDegree();
		if (val == 0) val = n1.index() - n2.index();
		return reverse ? -val : val;
	}
}
