package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.IPartitionedGraph;


public abstract class ColoringAlgorithm implements IBoundedAlgorithm {
	
	IPartitionedGraph graph;
	
	public ColoringAlgorithm(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
	
	public abstract void useColor(int node, int color) throws AlgorithmException;
}
