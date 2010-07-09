package pcp.entities.partitioned;

import java.util.Map;


public class PartitionedGraphTranslator {

	private final Map<Integer, Integer> oldToNewNode;
	private final Map<Integer, Integer> oldToNewPartition;
	
	PartitionedGraphTranslator(Map<Integer, Integer> oldToNewNode, Map<Integer, Integer> oldToNewPartition) {
		this.oldToNewNode = oldToNewNode;
		this.oldToNewPartition = oldToNewPartition;
	}

	public Integer translateNode(int nodeIndex) {
		return oldToNewNode.get(nodeIndex);
	}
	
	public Integer translatePartition(int partitionIndex) {
		return oldToNewPartition.get(partitionIndex);
	}
	
}
