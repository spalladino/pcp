package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.solver.cuts.CutFamily;


public class PartitionsColoring extends ColoringAlgorithm {
	
	public PartitionsColoring(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	public Integer getChi() {
		return graph.getPartitions().length;
	}

	@Override
	public Integer getColor(int node) throws AlgorithmException {
		throw new AlgorithmException("Color assignment for partition coloring not implemented.");
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
	public void useColorPartition(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Use color not supported for " + this.getClass().getName());
	}

	@Override
	public void forbidColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Forbidding nodes not supported for " + this.getClass().getName());
	}

}
