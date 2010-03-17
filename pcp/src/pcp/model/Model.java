package pcp.model;

import exceptions.AlgorithmException;
import ilog.concert.IloIntVar;
import ilog.concert.IloMPModeler;
import ilog.concert.IloObjective;
import pcp.algorithms.coloring.Coloring;
import pcp.entities.partitioned.PartitionedGraph;

public class Model {
	
	protected IloIntVar[][] xs;
	protected IloIntVar[] ws;
	protected IloObjective objective;
	
	protected PartitionedGraph graph;
	protected IloMPModeler modeler;
	
	protected int colors;
	protected Coloring coloring;
	protected BuilderStrategy strategy;
	
	public Model(PartitionedGraph graph) {
		this.graph = graph;
	}
	
	public Model(PartitionedGraph graph, int colors) {
		this(graph);
		this.colors = colors;
	}

	public boolean isTrivial() {
		return graph.getNodes().length == 0;
	}
	
	public IloIntVar[][] getXs() {
		return xs;
	}
	
	public IloIntVar[] getWs() {
		return ws;
	}
	
	public IloIntVar x(int i, int j) {
		return xs[i][j];
	}
	
	public IloIntVar w(int j) {
		return ws[j];
	}
	
	public IloObjective getObjective() {
		return objective;
	}
	
	public PartitionedGraph getGraph() {
		return graph;
	}

	public int getColorCount() {
		return this.colors;
	}
	
	public int getColor(int node) {
		try {
			return this.coloring.getColor(node);
		} catch (AlgorithmException e) {
			System.err.println("Error getting color " + e.toString());
			return 0;
		}
	}
	
	public int getNodeCount() {
		return this.graph.getNodes().length;
	}
	
	public Coloring getColoring() {
		return coloring;
	}

	
	public BuilderStrategy getStrategy() {
		return strategy;
	}

}
