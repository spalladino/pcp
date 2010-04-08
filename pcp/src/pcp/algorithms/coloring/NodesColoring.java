package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.entities.IPartitionedGraph;
import pcp.solver.cuts.CutFamily;


public class NodesColoring extends ColoringAlgorithm {
	
	public NodesColoring(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	public Integer getChi() {
		return graph.getNodes().length;
	}

	@Override
	public Integer getColor(int node) throws AlgorithmException {
		return node;
	}

	@Override
	public IAlgorithmBounder getBounder() {
		return null;
	}

	@Override
	public CutFamily getIdentifier() {
		return null;
	}
	
}
