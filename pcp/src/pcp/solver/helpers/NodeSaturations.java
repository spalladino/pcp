package pcp.solver.helpers;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.interfaces.IColorAssigner;
import props.Settings;
import exceptions.AlgorithmException;


public class NodeSaturations implements IColorAssigner {

	private IPartitionedGraph graph;
	private int[][] colorAdj;
	private int[] colorCount;

	private static final boolean colorAdjPartitions = Settings.get().getBoolean("dsatur.colorAdjPartitions");
	private static final boolean verifyConflicts = true;
	
	public NodeSaturations(IPartitionedGraph graph) {
		this.graph = graph;
		
		this.colorAdj = new int[graph.N()][graph.P()];
		this.colorCount = new int[graph.N()];
		
		if (colorAdjPartitions) {
			for (Node n : graph.getNodes()) {
				colorAdj[n.index][0] = graph.getNeighbourPartitions(n).length; 
			}
		} else {
			for (Edge e : graph.getEdges()) {
				colorAdj[e.index1()][0]++;
				colorAdj[e.index2()][0]++;
			}
		}
	}
	
	public void useColor(int node, int color) throws AlgorithmException {
		color++;
		
		if (verifyConflicts && colorAdj[node][color] > 0) {
			throw new AlgorithmException("Conflict on assign color");
		}
		for (Node n1 : graph.getNeighbours(graph.getNode(node))) {
			increaseColorCount(n1.index, color);
		}
	}
	
	public int getSaturation(int node) {
		return colorCount[node];
	}
	
	public int getUncoloredNeighbours(int node) {
		return colorAdj[node][0];
	}

	private void increaseColorCount(int node1, int color) throws AlgorithmException {
		
		if (colorAdj[node1][color] == 0) {
			colorCount[node1]++;
		}
		
		colorAdj[node1][color]++;
		colorAdj[node1][0]--;
		
		if (colorAdj[node1][0] < 0) {
			throw new AlgorithmException("Error on dsatur assign color");
		}
}
	
}
