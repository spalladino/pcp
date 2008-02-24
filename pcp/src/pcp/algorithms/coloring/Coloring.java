package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.interfaces.IPartitionedGraph;


public abstract class Coloring implements IBoundedAlgorithm {
	
	IPartitionedGraph graph;
	
	public Coloring(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
}
