package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;

public class DSaturPartitionColoringHardestPartition extends
		DSaturPartitionColoring {

	int[] partitionColorCount;
	int[][] partitionColorAdj;

	public DSaturPartitionColoringHardestPartition(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	protected Node getNextNode() {
		
		// Pick the hardest partition first
		int hardest = -1;
		
		for (Partition p : graph.getPartitions()) {
			if (handled[p.index()]) continue;
			
			// Hardest partition has greatest color count, among those, lowest count of unpainted neighbours
			if (hardest == -1 
				|| partitionColorCount[p.index()] > partitionColorCount[hardest]
                || (partitionColorCount[p.index()] == partitionColorCount[hardest] 
                  && partitionColorAdj[p.index()][0] < partitionColorAdj[hardest][0])) {
				hardest = p.index();
			}
		}
		
		// Once chosen, pick the easiest node
		Node minNode = null;
		for (Node n : graph.getPartitions()[hardest].getNodes()) {
			if (minNode == null 
				|| colorCount[n.index()] < colorCount[minNode.index()] 
                || (colorCount[n.index()] == colorCount[minNode.index()] 
                    && (colorAdj[n.index()][0] < colorAdj[minNode.index()][0]))) {
				minNode = n;
			}
		}
		
		return minNode;
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
	protected void increaseColorCount(Node n1, int color)
			throws AlgorithmException {
		super.increaseColorCount(n1, color);
		int part1 = n1.getPartition().index();

		if (partitionColorAdj[part1][color] == 0) {
			partitionColorCount[part1]++;
		}

		partitionColorAdj[part1][color]++;
		partitionColorAdj[part1][0]--;

		if (partitionColorAdj[part1][0] < 0) {
			throw new AlgorithmException("Error on dsatur assign color");
		}
	}

	@Override
	protected void decreaseColorCount(Node n1, int color)
			throws AlgorithmException {
		super.decreaseColorCount(n1, color);
		int part1 = n1.getPartition().index();

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
