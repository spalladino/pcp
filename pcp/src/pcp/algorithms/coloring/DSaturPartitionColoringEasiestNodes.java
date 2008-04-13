package pcp.algorithms.coloring;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;


public class DSaturPartitionColoringEasiestNodes extends DSaturPartitionColoring {
	
	public DSaturPartitionColoringEasiestNodes(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	protected Node getNextNode() {
		int max = -1;
		Node maxNode = null;
		
		for (Partition p : graph.getPartitions()) {
			if (handled[p.index()]) continue;
			
			// Pick the easiest node in the partition
			Node minNode = null;
			for (Node n : p.getNodes()) {
				if (minNode == null 
					|| colorCount[n.index()] < colorCount[minNode.index()] 
                    || (colorCount[n.index()] == colorCount[minNode.index()] 
                        && (colorAdj[n.index()][0] < colorAdj[minNode.index()][0]))) {
					minNode = n;
				}
			}
			
			// Check if it is the hardest one among the others
			if ((colorCount[minNode.index()] > max) || ((colorCount[minNode.index()] == max) && (colorAdj[minNode.index()][0] < colorAdj[maxNode.index()][0]))) {
				max = colorCount[minNode.index()];
				maxNode = minNode;
			}
		}
		
		return maxNode;
	}
	
	@Override
	protected void initFields()  {
		super.initFields();
	}
	
}
