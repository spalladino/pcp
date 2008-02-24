package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.interfaces.IPartitionedGraph;


public class PartitionsColoring extends Coloring {
	
	public PartitionsColoring(IPartitionedGraph graph) {
		super(graph);
	}

	@Override
	public Integer getChi() {
		return graph.getPartitions().length;
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
