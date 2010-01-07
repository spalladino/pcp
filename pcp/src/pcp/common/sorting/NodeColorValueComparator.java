package pcp.common.sorting;

import pcp.entities.Node;
import pcp.interfaces.IModelData;

public class NodeColorValueComparator extends BaseValueComparator<Node> {

	int color;
	
	public NodeColorValueComparator(IModelData data, int color) {
		super(data);
		this.color = color;
	}

	@Override
	public int compare(Node o1, Node o2) {
		try {
			return Double.compare(data.x(o1.index(),color), data.x(o2.index(),color));
		} catch (Exception e) {
			return 0;
		}
	}

}
