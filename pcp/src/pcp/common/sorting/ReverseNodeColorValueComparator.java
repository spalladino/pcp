package pcp.common.sorting;

import pcp.entities.partitioned.Node;
import pcp.interfaces.IModelData;

public class ReverseNodeColorValueComparator extends NodeColorValueComparator {

	public ReverseNodeColorValueComparator(IModelData data, int color) {
		super(data, color);
	}

	@Override
	public int compare(Node o1, Node o2) {
		return -super.compare(o1, o2);
	}

}
