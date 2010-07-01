package pcp.common.sorting;

import java.util.Comparator;

import pcp.entities.partitioned.Node;

public class NodeDSaturComparator implements Comparator<Node> {

	private final int[] colorCount;
	private final int[][] colorAdj;
	private final boolean reverse;
	
	public NodeDSaturComparator(int[][] colorAdj, int[] colorCount, boolean reverse) {
		this.colorAdj = colorAdj;
		this.colorCount = colorCount;
		this.reverse = reverse;
	}

	@Override
	public int compare(Node n1, Node n2) {
		int val = colorCount[n1.index()] - colorCount[n2.index()];
		if (val == 0) {
			val = (colorAdj[n1.index()][0] - colorAdj[n2.index()][0]);
		} return reverse ? -val : val;
	}

}
