package pcp.algorithms.coloring;

import java.io.PrintStream;
import java.util.List;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import exceptions.AlgorithmException;


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
	
	public int getIntColor(int node) throws AlgorithmException {
		Integer color = getColor(node);
		if (color == null) return -1;
		else return color;
	}
	
	public void printColoring(PrintStream stream) throws AlgorithmException {
		StringBuilder sb = new StringBuilder("Coloring: ");
		for (Node node : graph.getNodes()) {
			sb.append(node).append(":").append(getColor(node.index())).append(", ");
		} stream.println(sb.toString());
	}

	public void setInitialClique(List<pcp.entities.simple.Node> clique) throws AlgorithmException {
		int index = 0;
		// Every partition in the clique is assigned a color in ascending order 
		for(pcp.entities.simple.Node snode : clique) {
			useColorPartition(snode.index(), index);
			index++;
		}
	}

	protected int maxColors() {
		return this.graph.P() + 1;
	}
	
	public abstract Integer getChi() throws AlgorithmException;
	
	public abstract Integer getColor(int node) throws AlgorithmException;
	
	public abstract void useColor(int node, int color) throws AlgorithmException;
	
	public abstract void useColorPartition(int partition, int color) throws AlgorithmException;
	
	public abstract void forbidColor(int node, int color) throws AlgorithmException;

	public void setUpperBound(int bound) { }
	
	public void setLowerBound(int bound) { }
	
	public boolean hasSolution() { return true; }
}
