package pcp.common.sorting;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.IModelData;
import pcp.utils.DataUtils;

public class PartitionColorValueComparator extends BaseValueComparator<Partition> {

	private int color;
	private IPartitionedGraph graph;
	
	public PartitionColorValueComparator(IModelData data, int color, IPartitionedGraph graph) {
		super(data);
		this.color = color;
		this.graph = graph;
	}

	@Override
	public int compare(Partition arg0, Partition arg1) {
		return Double.compare(
				DataUtils.sumXi(graph.getNodes(arg0), color, data),
				DataUtils.sumXi(graph.getNodes(arg1), color, data));
	}

}
