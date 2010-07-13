package pcp.solver.branching;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.utils.IntUtils;
import props.Settings;


public class BasePrioritizer {
	
	
	final static int inputPartitionSizeFactor = Settings.get().getInteger("branch.prios.psize");
	final static int inputPartitionsAdjFactor = Settings.get().getInteger("branch.prios.psadjacent");
	final static int inputColorIndexFactor = Settings.get().getInteger("branch.prios.colorindex");
	
	final static int partitionSizeFactor = IntUtils.positiveOrZero(inputPartitionSizeFactor);
	final static int partitionsAdjFactor = IntUtils.positiveOrZero(inputPartitionsAdjFactor);
	final static int colorIndexFactor = IntUtils.positiveOrZero(inputColorIndexFactor);
	
	final static int reversePartitionSizeFactor = -IntUtils.negativeOrZero(inputPartitionSizeFactor);
	final static int reversePartitionsAdjFactor = -IntUtils.negativeOrZero(inputPartitionsAdjFactor);
	final static int reverseColorIndexFactor = -IntUtils.negativeOrZero(inputColorIndexFactor);
	
	PartitionedGraph graph;
	int colorCount;
	
	public BasePrioritizer(PartitionedGraph graph, int colorCount) {
		this.graph = graph;
		this.colorCount = colorCount;
	}

	public int getNodePriority(int i) {
		Node node = graph.getNode(i);
		return graph.getNeighbourPartitions(node).length * partitionsAdjFactor +
			graph.getNodes(node.getPartition()).length * partitionSizeFactor +
			(graph.P() - graph.getNeighbourPartitions(node).length) * reversePartitionsAdjFactor +
			(graph.N() - graph.getNodes(node.getPartition()).length) * reversePartitionSizeFactor;
	}
	
	public int getColorPriority(int j) {
		return (colorCount - j) * colorIndexFactor
		+ j * reverseColorIndexFactor;
	}
	
	public int getNodeColorPriority(int i, int j) {
		return getNodePriority(i) + getColorPriority(j);
	}
	
}
