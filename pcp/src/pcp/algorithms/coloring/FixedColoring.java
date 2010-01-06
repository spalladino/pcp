package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.interfaces.IPartitionedGraph;


public class FixedColoring extends Coloring {
	
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
	
	
	
}
