package pcp.common.sorting;

import pcp.entities.partitioned.Edge;
import pcp.interfaces.IModelData;

public class EdgeColorValueComparator extends BaseValueComparator<Edge> {

	int color;
	
	public EdgeColorValueComparator(IModelData data, int color) {
		super(data);
		this.color = color;
	}

	@Override
	public int compare(Edge arg0, Edge arg1) {
		return Double.compare(
				data.x(arg0.index1, color) + data.x(arg0.index2, color),
				data.x(arg1.index1, color) + data.x(arg1.index2, color));
	}

}
