package pcp.algorithms.coloring;

import pcp.entities.IPartitionedGraph;
import pcp.solver.cuts.CutFamily;
import exceptions.AlgorithmException;


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
	public CutFamily getIdentifier() {
		return null;
	}

	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Painting nodes not supported for " + this.getClass().getName());
	}

	@Override
	public void useColorPartition(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Use color nodes not supported for " + this.getClass().getName());
	}

	@Override
	public void forbidColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Forbidding nodes not supported for " + this.getClass().getName());
	}
	
}
