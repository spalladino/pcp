package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import props.Settings;

public class DSaturPartitionColoringHardestPartition extends
		DSaturPartitionColoring {

	int[] partitionColorCount;
	int[][] partitionColorAdj;

	private static final int colorCountWeight = Settings.get().getInteger("dsatur.partition.weight.colorCount");
	private static final int sizeWeight = Settings.get().getInteger("dsatur.partition.weight.size");
	private static final int uncoloredWeight = Settings.get().getInteger("dsatur.partition.weight.uncolored");
	
	public DSaturPartitionColoringHardestPartition(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	protected Node getNextNode() {
		
		// Pick the hardest partition first
		Partition hardest = null;
		int hardestWeight = -1;
		
		for (Partition p : graph.getPartitions()) {
			if (handled[p.index()]) continue;
			
			// Hardest partition has greatest weight calculated according to config params
			int partitionWeight = getWeight(p);
			if (partitionWeight > hardestWeight) { 
				hardest = p;
				hardestWeight = partitionWeight;
			}
		}
		
		// Once chosen, pick the easiest node
		Node minNode = null;
		for (Node n : graph.getNodes(graph.getPartitions()[hardest.index()])) {
			if (minNode == null 
				|| colorCount[n.index()] < colorCount[minNode.index()] 
                || (colorCount[n.index()] == colorCount[minNode.index()] 
                    && (colorAdj[n.index()][0] < colorAdj[minNode.index()][0]))) {
				minNode = n;
			}
		}
		
		return minNode;
	}

	private int getWeight(Partition p) {
		return (partitionColorCount[p.index()] * colorCountWeight) +
			((colorAdjPartitions ? graph.P() : graph.N()) - partitionColorAdj[p.index()][0]) * uncoloredWeight +
			(graph.getNodes(p).length * sizeWeight);
	}

	@Override
	protected void initFields() {
		super.initFields();
		partitionColorCount = new int[graph.P()];
		partitionColorAdj = new int[graph.P()][graph.P()];

		if (colorAdjPartitions) {
			for (Partition p : graph.getPartitions()) {
				partitionColorAdj[p.index()][0] = graph.getNeighbourPartitions(p).length; 
			}
		} else {
			for (Partition p : graph.getPartitions()) {
				partitionColorAdj[p.index()][0] = graph.getNeighbours(p).length; 
			}
		}
	}

	
	@Override
	protected void assignColor(Node node, int color) throws AlgorithmException {
		super.assignColor(node, color);
		
		for (Partition p : graph.getNeighbourPartitions(node)) {
			int part1 = p.index();
			
			if (partitionColorAdj[part1][color] == 0) {
				partitionColorCount[part1]++;
			}

			partitionColorAdj[part1][color]++;
			partitionColorAdj[part1][0]--;

			if (partitionColorAdj[part1][0] < 0) {
				throw new AlgorithmException("Error on dsatur assign color");
			}
		}
	}

	@Override
	protected void removeColor(Node node, int color) throws AlgorithmException {
		super.removeColor(node, color);
		
		for (Partition p : graph.getNeighbourPartitions(node)) {
			int part1 = p.index();

			partitionColorAdj[part1][color]--;

			if (partitionColorAdj[part1][color] == 0) {
				partitionColorCount[part1]--;
			}

			if (partitionColorAdj[part1][color] < 0) {
				throw new AlgorithmException("Error on dsatur remove color");
			}

			partitionColorAdj[part1][0]++;

		}

	}

}
