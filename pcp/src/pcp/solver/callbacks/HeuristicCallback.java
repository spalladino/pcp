package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.Factory;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import pcp.solver.helpers.PruneEvaluator;
import pcp.solver.heur.HeuristicMetrics;
import pcp.utils.ModelUtils;
import props.Settings;
import exceptions.AlgorithmException;

// TODO: Check generated solution complies with bounds set in current node as well as uses consecutive colors
public class HeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	static final Coloring coloringStrategy = BuilderStrategy.fromSettings().getColoring();
	
	static final boolean enabled = Settings.get().getBoolean("callback.heuristic.enabled");
	static final boolean primalEnabled = Settings.get().getBoolean("primal.enabled");
	
	static final double nodeLB = Settings.get().getDouble("primal.nodelb");
	static final int everynodes = Settings.get().getInteger("primal.everynodes");
	static final boolean logLeaf = Settings.get().getBoolean("logging.callback.leaf");
	
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
		if (PruneEvaluator.shouldPrune(model, nodesSet)) {
			setSolution(nodesSet);
		} else if (primalEnabled && super.getNnodes() > 1 && (super.getNnodes() % everynodes == 0)) {
			setPrimal(nodesSet);
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
			if (logLeaf) System.out.println("Pruning at " + nodesSet + " nodes set with " + coloring.getChi() + " coloring");
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}
	
	private void setPrimal(int nodesSet) {
		ColoringAlgorithm coloring = Factory.get().coloring(coloringStrategy, graph)
			.withBounder(new SolutionsBounder("coloring.primal"));
		
		try {
			int fixed = fillPrimalColoring(coloring);
			setLowerBound(coloring);
			createSolution(coloring);
			metrics.primalHeur(coloring, super.getNnodes(), fixed, nodesSet);
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
		boolean[] colored = new boolean[graph.P()];
		
		for (int i = 0; i < model.getNodeCount(); i++) {
			
			Node node = graph.getNode(i);
			if (colored[node.getPartition().index()]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				IloIntVar x = model.x(i, j);
				if (super.getFeasibility(x) != IntegerFeasibilityStatus.Infeasible && super.getLB(x) > 0.99) {
					colored[node.getPartition().index()] = true;
					coloring.useColor(i, j);
				}
			}
		}
	}
	
	private int fillPrimalColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		return fillPrimalColoringSafe(coloring);
	}

	private int fillPrimalColoringSafe(ColoringAlgorithm coloring)
			throws IloException, AlgorithmException {
		int fixedCount = 0;
		boolean[] colored = new boolean[graph.P()];
		
		// Iterate over nodes and colors checking for highest value among neighbours
		for (Node node : graph.getNodes()) {
			
			// Paint each partition only once regardless of the model
			if (colored[node.getPartition().index()]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				
				// Check if value is high enough to pass the lower bound
				double val = super.getValue(model.x(node.index(), j));
				if (val < nodeLB) continue;
				boolean isCandidate = true;
				
				// Check if it has the highest value among neighbours
				for (Node adj : graph.getNeighbours(node)) {
					if (val <= super.getValue(model.x(adj.index(), j))
					|| (val == super.getValue(model.x(adj.index(), j)) && node.index() > adj.index())) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Same for copartition
				for (Node adj : graph.getNodes(node.getPartition())) {
					if (adj.index() != node.index() && 
						((val <= super.getValue(model.x(adj.index(), j)))
						|| (val == super.getValue(model.x(adj.index(), j)) && node.index() > adj.index()))) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Use that color for the node
				coloring.useColor(node.index(), j);
				colored[node.getPartition().index()] = true;
				fixedCount++;
				break;
			}
		}
		
		return fixedCount;
	}

	@SuppressWarnings("unused")
	private int fillPrimalColoringFast(ColoringAlgorithm coloring)
			throws IloException, AlgorithmException {
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
