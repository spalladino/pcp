package pcp.algorithms.coloring;

import java.io.PrintStream;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.model.BuilderStrategy;
import pcp.model.strategy.Symmetry;


public class ColoringVerifier {

	final static BuilderStrategy strategy = BuilderStrategy.fromSettings();
	
	IPartitionedGraph graph;
	
	public ColoringVerifier(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public ColoringVerifier verify(ColoringAlgorithm coloring) throws AlgorithmException {
		int chi = coloring.getChi();
		
		for (Partition p : graph.getPartitions()) {
			int colored = 0;
			for (Node n : graph.getNodes(p)) {
				if (coloring.getColor(n.index) != null) {
					colored++;
				}
			}
			if (colored == 0) {
				throw new AlgorithmException("Partition " + p + " is not colored");
			}
		}
		
		for (Edge edge : graph.getEdges()) {
			Integer c1 = coloring.getColor(edge.index1);
			Integer c2 = coloring.getColor(edge.index2);
			if (c1 != null && c1 == c2) throw new AlgorithmException("Both nodes in edge " + edge + " have color " + c1);
		}
		
		int max = 0;
		for (Node node : graph.getNodes()) {
			Integer c = coloring.getColor(node.index);
			if (c != null && max < c) max = c;
		}
		
		if (max + 1 != chi) {
			throw new AlgorithmException("Number of different colors is " + (max + 1) + " and returned chi is " + chi);
		}
		
		if (strategy.getBreakSymmetry().equals(Symmetry.VerticesNumber)) {			
//			int j0, j1;
//			j0 = getNodeCount(coloring, 0);
//			
//			for (int j = 1; j < coloring.getChi(); j++) {
//				j1 = getNodeCount(coloring, j);
//				if (j1 > j0) {
//					throw new AlgorithmException("Color " + j + " uses " + j1 + " nodes while color " + (j-1) + " uses " + j0);
//				} j0 = j1;
//			}
		} else if (strategy.getBreakSymmetry().equals(Symmetry.MinimumNodeLabel)) {			
			int maxcolor = -1;
			for (int i = 0; i < graph.P(); i++) {
				Integer j = coloring.getPartitionColor(i);
				if (j != null) {
					if (j == maxcolor + 1) {
						maxcolor++;
					} else if (j > maxcolor + 1) {
						throw new AlgorithmException("Skipped color " + (maxcolor +1) + " in minimum label assignment");
					}
				}
			}
		}
		
		return this;
	}
	
	@SuppressWarnings("unused")
	private int getNodeCount(ColoringAlgorithm coloring, int color) throws AlgorithmException {
		int count = 0;
		for (int i = 0; i < graph.N(); i++) {
			if (coloring.getIntColor(i) == color) {
				count++;
			}
		} return count;
	}

	public void print(ColoringAlgorithm coloring) throws AlgorithmException {
		print(coloring, System.out);
	}

	private void print(ColoringAlgorithm coloring, PrintStream out) throws AlgorithmException {
		out.println("Chi = " + coloring.getChi());
		for (Node node : graph.getNodes()) {
			Integer color = coloring.getColor(node.index);
			if (color != null) {
				out.println(" " + node.getPartition().toString() + " " + node.toString() + ": " + color);
			}
		}
	}
	
}
