package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;


public class ColoringVerifier {

	IPartitionedGraph graph;
	
	public ColoringVerifier(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public void verify(Coloring coloring) throws AlgorithmException {
		int chi = coloring.getChi();
		
		for (Edge edge : graph.getEdges()) {
			int c1 = coloring.getColor(edge.index1());
			int c2 = coloring.getColor(edge.index2());
			if (c1 == c2) throw new AlgorithmException("Both nodes in edge " + edge + " have color " + c1);
		}
		
		int max = 0;
		for (Node node : graph.getNodes()) {
			int c = coloring.getColor(node.index());
			if (max < c) max = c;
		}
		
		if (max + 1 != chi) {
			throw new AlgorithmException("Number of different colors is " + (max + 1) + " and returned chi is " + chi);
		}
	}
	
}
