package pcp.model;

import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloMPModeler;
import ilog.concert.IloModel;
import ilog.concert.IloObjective;
import pcp.entities.partitioned.PartitionedGraph;

public class Model {
	
	protected IloIntVar[] allxs;
	protected IloIntVar[][] xs;
	protected IloIntVar[] ws;
	protected IloObjective objective;
	protected IloIntExpr colorsum;
	
	protected PartitionedGraph graph;
	protected IloMPModeler modeler;
	protected IloModel model;
	
	protected int colors;
	
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
	
	public IloModel getIloModel() {
		return model;
	}
	
	public IloIntVar[] getAllXs() {
		return allxs;
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
	
	public int getNodeCount() {
		return this.graph.getNodes().length;
	}
	
	public BuilderStrategy getStrategy() {
		return strategy;
	}
	
	public IloIntExpr getColorSum() {
		return colorsum;
	}

}
