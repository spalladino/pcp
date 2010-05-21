package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;


public class ColoringVerifier {

	IPartitionedGraph graph;
	
	public ColoringVerifier(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public void verify(ColoringAlgorithm coloring) throws AlgorithmException {
		int chi = coloring.getChi();
		
		for (Partition p : graph.getPartitions()) {
			int colored = 0;
			for (Node n : graph.getNodes(p)) {
				if (coloring.getColor(n.index()) != null) {
					colored++;
				}
			}
			if (colored == 0) {
				throw new AlgorithmException("Partition " + p + " is not colored");
			}
		}
		
		for (Edge edge : graph.getEdges()) {
			Integer c1 = coloring.getColor(edge.index1());
			Integer c2 = coloring.getColor(edge.index2());
			if (c1 != null && c1 == c2) throw new AlgorithmException("Both nodes in edge " + edge + " have color " + c1);
		}
		
		int max = 0;
		for (Node node : graph.getNodes()) {
			Integer c = coloring.getColor(node.index());
			if (c != null && max < c) max = c;
		}
		
		if (max + 1 != chi) {
			throw new AlgorithmException("Number of different colors is " + (max + 1) + " and returned chi is " + chi);
		}
	}
	
}
