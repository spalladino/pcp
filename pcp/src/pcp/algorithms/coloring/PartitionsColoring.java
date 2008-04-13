package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
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
	public IAlgorithmBounder getBounder() {
		return null;
	}

	@Override
	public CutFamily getIdentifier() {
		return null;
	}
	
	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Painting nodes not supported for " + this.getClass().getName());
	}

}
