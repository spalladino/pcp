package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.entities.IPartitionedGraph;


public class FixedColoring extends ColoringAlgorithm {
	
	int colors;
	
	public FixedColoring(IPartitionedGraph graph, int colors) {
		super(graph);
		this.colors = colors;
	}
	
	@Override
	public Integer getChi() {
		return this.colors;
	}

	@Override
	public Integer getColor(int node) throws AlgorithmException {
		throw new AlgorithmException("Color assignment for fixed coloring not implemented.");
	}

	@Override
	public IAlgorithmBounder getBounder() {
		return null;
	}

	@Override
	public Integer getIdentifier() {
		return null;
	}
	
	
	
}
