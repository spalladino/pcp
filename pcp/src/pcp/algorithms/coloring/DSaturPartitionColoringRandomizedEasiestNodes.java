package pcp.algorithms.coloring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pcp.common.sorting.NodeDSaturComparator;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.utils.IntUtils;
import pcp.utils.RandomUtils;
import exceptions.AlgorithmException;


public class DSaturPartitionColoringRandomizedEasiestNodes extends DSaturPartitionColoring {
	
	public DSaturPartitionColoringRandomizedEasiestNodes(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	protected Node getNextNode() throws AlgorithmException {
		List<Node> candidates = new ArrayList<Node>();
		
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
			
			// If there is no valid node selected throw
			if (minNode == null) {
				throw new AlgorithmException("No available nodes in partition");
			}
			
			// Add to candidates
			candidates.add(minNode);
		}
		
		// Sort by color degree desc and pick randomly
		Collections.sort(candidates, new NodeDSaturComparator(colorAdj, colorCount, true));
		int index = RandomUtils.pickGeometrical(IntUtils.min(5, candidates.size()));
		
		return candidates.get(index);
	}
	
	
}
