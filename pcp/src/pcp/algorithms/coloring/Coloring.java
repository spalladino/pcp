package pcp.algorithms.coloring;

import exceptions.AlgorithmException;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.IPartitionedGraph;


public abstract class Coloring implements IBoundedAlgorithm {
	
	IPartitionedGraph graph;
	
	public Coloring(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
}
