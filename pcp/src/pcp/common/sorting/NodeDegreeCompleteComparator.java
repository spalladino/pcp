package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.partitioned.Node;

public class NodeDegreeCompleteComparator implements Comparator<Node> {
	
	boolean reverse = false;
	
	public NodeDegreeCompleteComparator() {
	}
	
	public NodeDegreeCompleteComparator(boolean reverse) {
		this.reverse = reverse;
	}
	
	public int compare(Node n1, Node n2) {
		int val = n1.getDegree() - n2.getDegree();
		if (val == 0) val = n1.index - n2.index;
		return reverse ? -val : val;
	}
}
