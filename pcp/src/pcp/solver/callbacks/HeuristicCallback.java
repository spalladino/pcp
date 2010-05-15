package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.Factory;
import pcp.algorithms.bounding.IterationsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import pcp.solver.heur.HeuristicMetrics;
import props.Settings;
import exceptions.AlgorithmException;

public class HeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	static final Coloring coloringStrategy = BuilderStrategy.fromSettings().getColoring();
	
	static final boolean enabled = Settings.get().getBoolean("callback.heuristic.enabled");
	static final int pruningRemaining = Settings.get().getInteger("callback.pruning.remaining");
	static final double nodeLB = Settings.get().getDouble("primal.nodelb");
	
	IPartitionedGraph graph;
	Model model;
	
	HeuristicMetrics metrics;
	
	public HeuristicCallback() {
		metrics = new HeuristicMetrics();
	}
	
	public HeuristicCallback(Model model) {
		this();
		this.graph = model.getGraph();
		this.model = model;
	}
	
	public HeuristicMetrics getMetrics() {
		return metrics;
	}

	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		
		// Full run using current information if enough depth
		int nodesSet = countNodesEqualOne();
		if (nodesSet >= (model.getGraph().P() - pruningRemaining)) {
			setSolution(nodesSet);
		} 
		
		// TODO: Define primal heuristic criteria and run
	}
	
	private void setSolution(int nodesSet) {
		ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph);
		
		try {
			fillLeafColoring(coloring);
			setBounds(coloring);
			createSolution(coloring);
			metrics.leafHeur(coloring, nodesSet);
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}
	
	private void setPrimal() {
		ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph).withBounder(new IterationsBounder("coloring.primal"));
		
		try {
			fillPrimalColoring(coloring);
			setBounds(coloring);
			createSolution(coloring);
			metrics.primalHeur(coloring);
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}

	private void setBounds(ColoringAlgorithm coloring) throws IloException {
		// Set upper bound as objective value of global incumbent
		double upper = super.getIncumbentObjValue();
		if (!Double.isNaN(upper) && upper != 0.0) {
			coloring.setUpperBound((int) Math.ceil(upper));
		}
		
		// Set lower bound as objective value of current relaxation
		double lower = super.getObjValue();
		if (!Double.isNaN(lower) && lower != 0.0) {
			coloring.setLowerBound((int) Math.ceil(lower));
		}
	}

	private void createSolution(ColoringAlgorithm coloring) throws AlgorithmException, IloException {
		int n = model.getNodeCount();
		int c = model.getColorCount();
		
		Integer chi = coloring.getChi();
		if (chi == null || !coloring.hasSolution()) return;
		
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
				vals[idx] = coloring.getIntColor(i) == j ? 1 : 0;
				idx++;
			}
		}
		
		super.setSolution(vars, vals);
	}
	
	private void fillLeafColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getFeasibility(x) != IntegerFeasibilityStatus.Infeasible && super.getLB(x) > 0.99) {
					coloring.useColor(i, j);
				}
			}
		}
	}
	
	private void fillPrimalColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getValue(x) > nodeLB) {
					coloring.useColor(i, j);
				}
			}
		}
	}
	
	private int countNodesEqualOne() throws IloException {
		int count = 0;
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getFeasibility(x) != IntegerFeasibilityStatus.Infeasible && super.getLB(x) > 0.99) count++;
			}
		}
		return count;
	}
	
}
