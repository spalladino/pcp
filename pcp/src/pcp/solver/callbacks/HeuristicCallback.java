package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.Factory;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import pcp.solver.heur.HeuristicMetrics;
import pcp.utils.ModelUtils;
import props.Settings;
import exceptions.AlgorithmException;

public class HeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	static final Coloring coloringStrategy = BuilderStrategy.fromSettings().getColoring();
	
	static final boolean enabled = Settings.get().getBoolean("callback.heuristic.enabled");
	static final boolean primalEnabled = Settings.get().getBoolean("primal.enabled");
	
	static final int pruningRemaining = Settings.get().getInteger("pruning.remaining");
	static final double nodeLB = Settings.get().getDouble("primal.nodelb");
	static final int everynodes = Settings.get().getInteger("primal.everynodes");
	
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
		
		// Full run using current information if enough depth, or primal if frequency
		int nodesSet = countNodesEqualOne();
		if (nodesSet >= (model.getGraph().P() - pruningRemaining)) {
			setSolution(nodesSet);
		} else if (primalEnabled && super.getNnodes() > 1 && (super.getNnodes() % everynodes == 0)) {
			setPrimal();
		}
	}
	
	private void setSolution(int nodesSet) {
		ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph);
		
		try {
			fillLeafColoring(coloring);
			setLowerBound(coloring);
			setUpperBound(coloring);
			createSolution(coloring);
			metrics.leafHeur(coloring, nodesSet);
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}
	
	private void setPrimal() {
		ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph)
			.withBounder(new SolutionsBounder("coloring.primal"));
		
		try {
			int fixed = fillPrimalColoring(coloring);
			setLowerBound(coloring);
			createSolution(coloring);
			metrics.primalHeur(coloring, super.getNnodes(), fixed);
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}

	private void setLowerBound(ColoringAlgorithm coloring) throws IloException {
		// Set lower bound as objective value of current relaxation
		double lower = super.getObjValue();
		if (!Double.isNaN(lower) && lower != 0.0) {
			coloring.setLowerBound((int) Math.ceil(lower));
		}
	}

	private void setUpperBound(ColoringAlgorithm coloring) throws IloException {
		// Set upper bound as objective value of global incumbent
		double upper = super.getIncumbentObjValue();
		if (!Double.isNaN(upper) && upper != 0.0) {
			coloring.setUpperBound((int) Math.ceil(upper));
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
	
	private int fillPrimalColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		int fixedCount = 0;
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i, j);
				if (super.getValue(x) > nodeLB) {
					fixedCount++;
					coloring.useColor(i, j);
				}
			}
		} return fixedCount;
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);
	}


	
}
