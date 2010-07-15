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
	
	protected List<pcp.entities.simple.Node> clique;
	
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
		return (color == null) ? -1 : color;
	}
	
	public Integer getPartitionColor(int partition) throws AlgorithmException {
		for (Node node : graph.getNodes(graph.getPartition(partition))) {
			Integer color = getColor(node.index);
			if (color != null) return color;
		} return null;
	}
	
	public Integer getPartitionIntColor(int partition) throws AlgorithmException {
		Integer color = getPartitionColor(partition);
		return (color == null) ? -1 : color;
	}
	
	public int getNodeCount(int color) throws AlgorithmException {
		int count = 0;
		for (int i = 0; i < graph.N(); i++) {
			if (getIntColor(i) == color) {
				count++;
			}
		} return count;
	}
	
	public int getMinIndex(Integer color) throws AlgorithmException {
		for (int i = 0; i < graph.N(); i++) {
			if (getIntColor(i) == color) {
				return i;
			}
		} return -1;
	}
	
	public int getMinPartitionIndex(Integer color) throws AlgorithmException {
		for (int i = 0; i < graph.P(); i++) {
			if (getPartitionIntColor(i) == color) {
				return i;
			}
		} return -1;
	}

	
	public void printColoring(PrintStream stream) throws AlgorithmException {
		StringBuilder sb = new StringBuilder("Coloring: ");
		for (Node node : graph.getNodes()) {
			sb.append(node).append(":").append(getColor(node.index)).append(", ");
		} stream.println(sb.toString());
	}

	public void setInitialClique(List<pcp.entities.simple.Node> clique) throws AlgorithmException {
		int index = 0;
		this.clique = clique;
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
