package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.IPartitionedGraph;


public abstract class ColoringAlgorithm implements IBoundedAlgorithm {
	
	protected IPartitionedGraph graph;
	protected IAlgorithmBounder bounder;
	
	public ColoringAlgorithm(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	@Override
	public IAlgorithmBounder getBounder() {
		return this.bounder;
	}

	public ColoringAlgorithm withBounder(IAlgorithmBounder bounder) {
		this.bounder = bounder;
		return this;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
	
	public abstract void useColor(int node, int color) throws AlgorithmException;

}
