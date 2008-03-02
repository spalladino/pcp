package pcp.common.sorting;

import pcp.entities.partitioned.Partition;
import pcp.interfaces.IModelData;

public class PartitionNodeSumComparator extends BaseValueComparator<Partition> {

	public PartitionNodeSumComparator(IModelData data) {
		super(data);
	}

	@Override
	public int compare(Partition arg0, Partition arg1) {
		
		return 0;
	}

}
