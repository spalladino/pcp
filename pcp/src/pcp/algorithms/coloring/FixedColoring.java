package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.entities.IPartitionedGraph;
import pcp.solver.cuts.CutFamily;


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
	public CutFamily getIdentifier() {
		return null;
	}
	
	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Painting nodes not supported for " + this.getClass().getName());
	}

	
	
	
}
