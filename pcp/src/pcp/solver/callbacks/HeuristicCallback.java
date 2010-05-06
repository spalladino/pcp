package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import pcp.Factory;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import props.Settings;
import exceptions.AlgorithmException;

public class HeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	static final Coloring coloringStrategy = BuilderStrategy.fromSettings().getColoring();
	
	static final boolean log = Settings.get().getBoolean("logging.callback.heuristic");
	static final boolean enabled = Settings.get().getBoolean("callback.heuristic.enabled");
	static final int pruningRemaining = Settings.get().getInteger("callback.pruning.remaining");
	
	IPartitionedGraph graph;
	Model model;
	
	public HeuristicCallback() {
		
	}
	
	public HeuristicCallback(Model model) {
		this.graph = model.getGraph();
		this.model = model;
	}
	
	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		
		// Full run using current information if enough depth
		if (countNodesEqualOne() >= (model.getGraph().P() - pruningRemaining)) {
			setSolution();
		}
	}
	
	private void setSolution() {
		try {
			ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph);
			fillColoring(coloring);
			createSolution(coloring);
			if (log) System.out.println("Using brute force at " + countNodesEqualOne() + " nodes set returning coloring of " + coloring.getChi());
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}

	private void createSolution(ColoringAlgorithm coloring) throws AlgorithmException, IloException {
		int n = model.getNodeCount();
		int c = model.getColorCount();
		int chi = coloring.getChi();
		
		double[] vals = new double[n * c + c];
		IloNumVar[] vars = new IloNumVar[n * c + c];
		
		int idx = 0;
		for (int j = 0; j < c; j++) {
			// Set w variable value
			vars[idx] = model.getWs()[j];
			vals[idx] = j < chi ? 1 : 0;
			idx++;
			for (int i = 0; i < n; i++) {
				// Set x variable value
				vars[idx] = model.getXs()[i][j];
				vals[idx] = coloring.getColor(i) == j ? 1 : 0;
				idx++;
			}
		}
		
		super.setSolution(vars, vals);
	}
	
	private void fillColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getLB(x) > 0.99) coloring.useColor(i, j);
			}
		}
	}
	
	private int countNodesEqualOne() throws IloException {
		int count = 0;
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getLB(x) > 0.99) count++;
			}
		}
		return count;
	}
	
}
