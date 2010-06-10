package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;


public class DSaturPartitionColoringEasiestNodes extends DSaturPartitionColoring {
	
	public DSaturPartitionColoringEasiestNodes(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	protected Node getNextNode() throws AlgorithmException {
		int max = -1;
		Node maxNode = null;
		
		for (Partition p : graph.getPartitions()) {
			if (partitionsHandled[p.index()]) {
				continue;
			}
			
			// Pick the easiest node in the partition
			Node minNode = null;
			for (Node n : graph.getNodes(p)) {
				if (nodesHandled[n.index()]) {
					continue;
				}
				
				if (minNode == null 
					|| colorCount[n.index()] < colorCount[minNode.index()] 
                    || (colorCount[n.index()] == colorCount[minNode.index()] 
                        && (colorAdj[n.index()][0] < colorAdj[minNode.index()][0]))) {
					minNode = n;
				}
			}
			
			// If there is no valid node selected continue with next partition
			if (minNode == null) {
				throw new AlgorithmException("No available nodes in partition");
			}
			
			// Check if it is the hardest one among the others
			if ((colorCount[minNode.index()] > max) || ((colorCount[minNode.index()] == max) && (colorAdj[minNode.index()][0] < colorAdj[maxNode.index()][0]))) {
				max = colorCount[minNode.index()];
				maxNode = minNode;
			}
		}
		
		return maxNode;
	}
	
	
}
