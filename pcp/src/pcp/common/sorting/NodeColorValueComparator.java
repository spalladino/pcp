package pcp.common.sorting;

import pcp.entities.partitioned.Node;
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
			int cmp = Double.compare(data.x(o1.index,color), data.x(o2.index,color));
			if (cmp == 0) {
				return o1.index - o2.index; 
			} else {
				return cmp;
			}
		} catch (Exception e) {
			return 0;
		}
	}

}
