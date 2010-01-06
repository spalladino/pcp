package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.interfaces.IPartitionedGraph;


public abstract class Coloring {
	
	IPartitionedGraph graph;
	
	public Coloring(IPartitionedGraph graph) {
		this.graph = graph;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
}
