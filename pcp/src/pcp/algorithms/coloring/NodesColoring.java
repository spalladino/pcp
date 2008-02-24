package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.interfaces.IPartitionedGraph;


public class NodesColoring extends Coloring {
	
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
	public Integer getIdentifier() {
		return null;
	}
	
}
