package pcp.algorithms.coloring;

import java.util.Arrays;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.solver.cuts.CutFamily;
import exceptions.AlgorithmException;

public class BruteForcePartitionColoring extends ColoringAlgorithm {

	int[] counts;
	int[] colors;
	boolean[] fixed;
	boolean[] handled;
	
	int[] bestColors;
	int bestCount = Integer.MAX_VALUE;
	
	int count = 0;
	
	public BruteForcePartitionColoring(IPartitionedGraph graph) {
		super(graph);
		counts = new int[graph.N()+1];
		colors = new int[graph.N()];
		fixed = new boolean[graph.N()];
		handled = new boolean[graph.P()];
	}

	private void color(int partitionIndex) {
		if (partitionIndex == graph.P()) {
			if (count < bestCount) {
				bestColors = colors.clone();
				bestCount = count;
			} return;
		}
		
		if (count > bestCount) {
			return;
		}
		
		if (handled[partitionIndex]) {
			color(partitionIndex + 1);
			return;
		}
		
		Partition partition = graph.getPartitions()[partitionIndex];
		for (Node node : graph.getNodes(partition)) {
			for (int j = 1; j <= node.getDegree() + 1; j++) {
				
				// Check if color j can be assigned to node
				boolean valid = true;
				for (Node neighbour : graph.getNeighbours(node)) {
					if (colors[neighbour.index()] == j) {
						valid = false;
						break;
					}
				}
				if (!valid) { 
					continue;
				}
				
				// Assign color, backtrack, and unassign
				assignColor(node, j);
				color(partitionIndex + 1);
				unassignColor(node, j);
				
			}
		}
	}
	
	private void assignColor(Node node, int j) {
		colors[node.index()] = j;
		if (counts[j] == 0) count++;
		counts[j]++;
	}
	
	private void unassignColor(Node node, int j) {
		colors[node.index()] = 0;
		counts[j]--;
		if (counts[j] == 0) count--;
	}

	@Override
	public Integer getChi() throws AlgorithmException {
		if (bestCount == Integer.MAX_VALUE) color(0);
		return bestCount;
	}

	@Override
	public Integer getColor(int node) throws AlgorithmException {
		return bestColors[node] == 0 ? null : bestColors[node] - 1;
	}

	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		fixed[node] = true;
		handled[graph.getNode(node).getPartition().index()] = true;
		assignColor(graph.getNode(node), color+1);
	}

	@Override
	public CutFamily getIdentifier() {
		return null;
	}
	
	public String getColorClassString() {
		return "Chi = " + bestCount + ": " + Arrays.toString(bestColors);
	}

	@Override
	public void useColorPartition(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Use color not supported for " + this.getClass().getName());
	}

	@Override
	public void forbidColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Forbidding nodes not supported for " + this.getClass().getName());
	}

}
