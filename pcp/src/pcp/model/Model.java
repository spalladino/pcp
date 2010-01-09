package pcp.model;

import ilog.concert.IloIntVar;
import ilog.concert.IloMPModeler;
import ilog.concert.IloObjective;
import pcp.entities.PartitionedGraph;

public class Model {
	
	protected IloIntVar[][] xs;
	protected IloIntVar[] ws;
	protected IloObjective objective;
	
	protected PartitionedGraph graph;
	protected IloMPModeler modeler;
	
	protected int colors;
	
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
	
	public int getNodeCount() {
		return this.graph.getNodes().length;
	}

}
