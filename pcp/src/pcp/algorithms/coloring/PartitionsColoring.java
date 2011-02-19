package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Partition;
import pcp.solver.cuts.CutFamily;


public class PartitionsColoring extends ColoringAlgorithm {
	
	Integer[] nodeColors;
	boolean[] usedColors;
	boolean[] coloredPartitions;
	int chi;
	
	public PartitionsColoring(IPartitionedGraph graph) {
		super(graph);
		this.coloredPartitions = new boolean[graph.P()];
		this.nodeColors = new Integer[graph.N()];
		this.usedColors = new boolean[graph.P()];
		this.chi = 0;
	}

	@Override
	public Integer getChi() {
		if (this.chi == 0) {
			this.chi = assignColors();
		} return this.chi;
	}

	@Override
	public Integer getColor(int node) throws AlgorithmException {
		getChi();
		return nodeColors[node];
	}

	@Override
	public CutFamily getIdentifier() {
		return null;
	}
	
	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Painting nodes not supported for " + this.getClass().getName());
	}

	@Override
	public void useColorPartition(int partition, int color) throws AlgorithmException {
		if (usedColors[color]) throw new AlgorithmException("Cannot reassign color in trivial partition coloring");
		nodeColors[getFirstNode(partition)] = color;
		coloredPartitions[partition] = true;
		usedColors[color] = true;
	}

	@Override
	public void forbidColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Forbidding nodes not supported for " + this.getClass().getName());
	}

	@Override
	public boolean isOptimalSolution() {
		return false;
	}

	private int assignColors() {
		int color = 0;
		for (Partition p : graph.getPartitions()) {
			if (!coloredPartitions[p.index]) {
				coloredPartitions[p.index] = true;
				while (usedColors[color]) color++;
				nodeColors[getFirstNode(p)] = color;
				usedColors[color] = true;
			}
		} return color;
	}

	private int getFirstNode(int partition) {
		return graph.getNodes(graph.getPartition(partition))[0].index;
	}
	
	private int getFirstNode(Partition partition) {
		return graph.getNodes(partition)[0].index;
	}
}
