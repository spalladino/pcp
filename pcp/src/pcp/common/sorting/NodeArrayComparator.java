package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.partitioned.Node;


public class NodeArrayComparator implements Comparator<Node[]> {
	public int compare(Node[] n1, Node[] n2) {
		if (n1.length < n2.length) return -1;
		else if (n1.length > n2.length) return 1;
		for (int i = 0; i < n1.length; i++) {
			if (n1[i].index < n2[i].index) return -1;
			else if (n1[i].index > n2[i].index) return 1;
		} return 0;
	}
}
