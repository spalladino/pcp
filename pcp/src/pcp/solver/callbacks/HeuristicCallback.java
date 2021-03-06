package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;

import java.util.Arrays;

import pcp.Factory;
import pcp.algorithms.bounding.RemainingTimeBounder;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.IPrimalSolutionProvider;
import pcp.interfaces.ITimeProvider;
import pcp.interfaces.IColorAssigner.DSaturAssignment;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.ModelVerifier;
import pcp.model.strategy.Coloring;
import pcp.model.strategy.Objective;
import pcp.solver.data.NodeData;
import pcp.solver.helpers.PruneEvaluator;
import pcp.solver.heur.HeuristicMetrics;
import pcp.utils.DoubleUtils;
import pcp.utils.ModelUtils;
import props.Settings;
import exceptions.AlgorithmException;

public class HeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	private static final Objective objectiveStrategy = BuilderStrategy.fromSettings().getObjective();
	
	private static final boolean primalEnabled = Settings.get().getBoolean("primal.enabled");
	private static final boolean onlyOnUp = Settings.get().getBoolean("primal.onlyonup");
	private static final double nodeLB = Settings.get().getDouble("primal.nodelb");
	private static final int everynodes = Settings.get().getInteger("primal.everynodes");
	private static final boolean pruneUseUB = Settings.get().getBoolean("pruning.useub");
	private static final boolean primalUseUB = Settings.get().getBoolean("primal.useub");
	private static final boolean runOnCutCallback = Settings.get().getBoolean("primal.runoncutcallback");
	
	private static final boolean validateSolutions = Settings.get().getBoolean("validate.heuristics");
	private static final boolean logLeaf = Settings.get().getBoolean("logging.callback.leaf");
	private static final boolean logHeur = Settings.get().getBoolean("logging.callback.heuristic");
	private static final boolean logBounds = Settings.get().getBoolean("logging.bounds");
	private static final boolean logNodesSet = Settings.get().getBoolean("logging.nodesset");

	private static final Coloring primalColoringStrategy = Settings.get().getEnum("primal.dsatur.coloring", Coloring.class);
	private static final Coloring pruningColoringStrategy = Settings.get().getEnum("pruning.coloring", Coloring.class);
	private static final DSaturAssignment assignment = Settings.get().getEnum("primal.dsatur.assign", DSaturAssignment.class);
	
	IPartitionedGraph graph;
	Model model;
	ModelVerifier verifier;
	
	HeuristicMetrics metrics;
	IPrimalSolutionProvider primalProvider;
	ITimeProvider time;
	
	public HeuristicCallback() {
		metrics = new HeuristicMetrics();
	}
	
	public HeuristicCallback(Model model, IPrimalSolutionProvider primalProvider, ITimeProvider time) {
		this();
		this.graph = model.getGraph();
		this.model = model;
		this.primalProvider = primalProvider;
		this.verifier = new ModelVerifier(model.getIloModel());
		this.time = time;
	}
	
	public HeuristicMetrics getMetrics() {
		return metrics;
	}

	@Override
	protected void main() throws IloException {
		if (logBounds) logBounds();

		// Full run using current information if enough depth, or primal if frequency
		int nodesSet = countNodesEqualOne();
		if (logNodesSet) System.out.println("Nodes set: " + nodesSet);
		if (PruneEvaluator.shouldPrune(model, nodesSet)) {
			setSolution(nodesSet);
		} else if (runOnCutCallback && primalProvider != null) {
			if (primalProvider.getPrimalChi() != null) {
				if (logHeur) System.out.println("Assigning primal with chi " + primalProvider.getPrimalChi() + " in heuristic callback obtained during cut callback");
				super.setSolution(primalProvider.getPrimalVars(), primalProvider.getPrimalVals());
			}
		} else if (primalEnabled && super.getNnodes() > 1 && (
				(!onlyOnUp && super.getNnodes() % everynodes == 0) ||
				(onlyOnUp && NodeData.getDirection(super.getNodeData()) == 1))) {
			setPrimal(nodesSet);
		}
		
	}

	private void logBounds() throws IloException {
		System.out.println("LBs: " + Arrays.toString(super.getLBs(model.getAllXs())));
		System.out.println("UBs: " + Arrays.toString(super.getUBs(model.getAllXs())));
	}
	
	private void setSolution(int nodesSet) throws IloException {
		ColoringAlgorithm coloring = Factory.get()
			.coloring(pruningColoringStrategy, graph)
			.withBounder(new RemainingTimeBounder(this.time, getEndTime()));

		try {
			fillLeafColoring(coloring);
			setLowerBound(coloring);
			if (pruneUseUB) setUpperBound(coloring);
			if (coloring.isOptimalSolution()) {
				createSolution(coloring);
				metrics.leafHeur(coloring, nodesSet, true);
				if (logLeaf) System.out.println("Pruning at " + nodesSet + " nodes set with " + coloring.getChi() + " coloring");
			} else {
				metrics.leafHeur(coloring, nodesSet, false);
				if (logLeaf) System.out.println("Pruning at " + nodesSet + " nodes set with " + coloring.getChi() + " coloring did not finish or failed to improve bound");
			}
			
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}
	
	private void setPrimal(int nodesSet) {
		ColoringAlgorithm coloring = Factory.get()
			.coloring(primalColoringStrategy, graph)
			.withBounder(new SolutionsBounder("coloring.primal"));
		
		try {
			int fixed = assignColorsFromSolution(coloring);
			setLowerBound(coloring);
			if (primalUseUB) setUpperBound(coloring);
			createSolution(coloring);
			metrics.primalHeur(coloring, super.getNnodes(), fixed, nodesSet);
			//if (logHeur) System.out.println("Executed primal heuristic having fixed " + fixed + " nodes and obtained solution of chi " + (chi == null ? "none" : chi));
		} catch (Exception ex) {
			pcp.Logger.error("Exception in heuristic callback", ex);
		}
	}

	private void setLowerBound(ColoringAlgorithm coloring) throws IloException {
		// Set lower bound as objective value of current relaxation
		double lower = objectiveStrategy.equals(Objective.Equal) 
			? super.getObjValue()
			: super.getValue(model.getColorSum());
		if (!Double.isNaN(lower) && lower != 0.0) {
			coloring.setLowerBound(DoubleUtils.ceil(lower));
			if (logHeur) System.out.println("Using lower bound: " + DoubleUtils.ceil(lower));
		}
	}

	private void setUpperBound(ColoringAlgorithm coloring) throws IloException {
		// Set upper bound as objective value of incumbent
		double upper = objectiveStrategy.equals(Objective.Equal) 
			? super.getIncumbentObjValue()
			: DoubleUtils.sum(super.getIncumbentValues(model.getWs()));
		if (!Double.isNaN(upper) && upper != 0.0) {
			coloring.setUpperBound(DoubleUtils.ceil(upper));
			if (logHeur) System.out.println("Using upper bound: " + DoubleUtils.ceil(upper));
		}
	}

	private Integer createSolution(ColoringAlgorithm coloring) throws AlgorithmException, IloException {
		int n = model.getNodeCount();
		int c = model.getColorCount();
		
		Integer chi = coloring.getChi();
		if (chi == null || !coloring.hasSolution()) return null;
		
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

		if (validateSolutions) {
			verifier.verify(vars, vals);
			validateSolution(vars, vals);
		}
		
		super.setSolution(vars, vals);
		return chi;
	}
	
	private void validateSolution(IloNumVar[] vars, double[] vals) throws IloException, AlgorithmException {
		for (int i = 0; i < vars.length; i++) {
			IloNumVar var = vars[i];
			double d = vals[i];
			
			if (d < var.getLB() || d > var.getUB()) {
				logBounds();
				throw new AlgorithmException("Invalid solution " + d + " for variable " + var + " bounds [" + var.getLB() + "," + var.getUB() + "] with relaxation value " + super.getValue(var));
			}
		}
	}

	private void fillLeafColoring(ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		boolean[] colored = new boolean[graph.P()];
		
		for (int i = 0; i < model.getNodeCount(); i++) {
			
			Node node = graph.getNode(i);
			
			if (colored[node.getPartition().index]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				IloIntVar x = model.x(i, j);
				if (super.getFeasibility(x) != IntegerFeasibilityStatus.Infeasible && super.getLB(x) > 0.99) {
					colored[node.getPartition().index] = true;
					coloring.useColor(i, j);
					break;
				}  else if (super.getUB(x) == 0.0) {
					coloring.forbidColor(i, j);
				}
			}
		}
	}

	private int assignColorsFromSolution(ColoringAlgorithm coloring)
		throws IloException, AlgorithmException {
		
		switch(assignment) {
			case CheckAdjs: return assignColorsFromSolutionCheckingAdjs(coloring);
			case Fast: return assignColorsFromSolutionFast(coloring);
			case Safe: return assignColorsFromSolutionSafe(coloring);
			default: throw new AlgorithmException("Unknown dsatur assignment enum " + assignment);
		}
	}
	
	private int assignColorsFromSolutionCheckingAdjs(ColoringAlgorithm saturs)
			throws IloException, AlgorithmException {
		int fixedCount = 0;
		for (Partition p : graph.getPartitions()) {
			part: for (Node n : graph.getNodes(p)) {
				int i = n.index;
				color: for (int j = 0; j < model.getColorCount(); j++) {
					IloIntVar x = model.x(i, j);
					double xval = super.getValue(x);
					if (xval > nodeLB) {
						for (Node adj : graph.getNeighbours(n)) {
							if (super.getValue(model.x(adj.index, j)) > xval) {
								continue color;
							}
						}
						
						fixedCount++;
						saturs.useColor(i, j);
						break part;
					} else if (super.getUB(x) == 0.0) {
						saturs.forbidColor(i, j);
					}
				}				
			}
		}
		return fixedCount;
	}
	
	private int assignColorsFromSolutionSafe(ColoringAlgorithm coloring)
			throws IloException, AlgorithmException {
		int fixedCount = 0;
		boolean[] colored = new boolean[graph.P()];
		
		// Iterate over nodes and colors checking for highest value among neighbours
		for (Node node : graph.getNodes()) {
			
			// Paint each partition only once regardless of the model
			if (colored[node.getPartition().index]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				
				// Forbid color if it must
				if (super.getUB(model.x(node.index, j)) == 0.0) {
					coloring.forbidColor(node.index, j);
					continue;
				}
				
				// Check if value is high enough to pass the lower bound
				double val = super.getValue(model.x(node.index, j));
				if (val < nodeLB) continue;
				boolean isCandidate = true;
				
				// Check if it has the highest value among neighbours
				for (Node adj : graph.getNeighbours(node)) {
					if (val <= super.getValue(model.x(adj.index, j))
					|| (val == super.getValue(model.x(adj.index, j)) && node.index > adj.index)) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Same for copartition
				for (Node adj : graph.getNodes(node.getPartition())) {
					if (adj.index != node.index && 
						((val <= super.getValue(model.x(adj.index, j)))
						|| (val == super.getValue(model.x(adj.index, j)) && node.index > adj.index))) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Use that color for the node
				coloring.useColor(node.index, j);
				colored[node.getPartition().index] = true;
				fixedCount++;
				break;
			}
		}
		
		return fixedCount;
	}

	private int assignColorsFromSolutionFast(ColoringAlgorithm coloring)
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
