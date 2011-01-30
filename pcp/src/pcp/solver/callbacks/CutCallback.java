package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.AlgorithmException;

import pcp.Factory;
import pcp.algorithms.block.BlockColorCuts;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.algorithms.clique.ExtendedCliqueCutter;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.holes.ComponentHolesCuts;
import pcp.algorithms.holes.HolesCuts;
import pcp.algorithms.iset.ComponentPathDetector;
import pcp.algorithms.iset.SimplePathDetector;
import pcp.definitions.Comparisons;
import pcp.definitions.Constants;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IExecutionDataProvider;
import pcp.interfaces.IModelData;
import pcp.interfaces.IPrimalSolutionProvider;
import pcp.interfaces.IColorAssigner.DSaturAssignment;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Coloring;
import pcp.model.strategy.Objective;
import pcp.solver.cuts.CutFamily;
import pcp.solver.cuts.CutsMetrics;
import pcp.solver.data.Iteration;
import pcp.solver.data.NodeData;
import pcp.solver.helpers.PruneEvaluator;
import pcp.solver.heur.HeuristicMetrics;
import pcp.solver.io.IterationPrinter;
import pcp.utils.DoubleUtils;
import pcp.utils.IntUtils;
import pcp.utils.ModelUtils;
import props.Settings;


public class CutCallback extends IloCplex.CutCallback implements Comparisons, ICutBuilder, IModelData, IExecutionDataProvider, IPrimalSolutionProvider {

	final static Objective objectiveStrategy = BuilderStrategy.fromSettings().getObjective();
	
	final static boolean checkViolatedCut = Settings.get().getBoolean("validate.cutsViolated");
	final static boolean useBreakingSymmetry = Settings.get().getBoolean("cuts.iset.useBreakingSymmetry");
	final static boolean usePathsAlgorithm = Settings.get().getBoolean("cuts.iset.usePathsAlgorithm");	
	final static boolean local = Settings.get().getBoolean("cuts.local");
	
	final static boolean logIterData = Settings.get().getBoolean("logging.iterData");
	final static boolean logIterColors = Settings.get().getBoolean("logging.iterColors");
	final static boolean logIneqs = Settings.get().getBoolean("logging.ineqs");
	final static boolean logCallback = Settings.get().getBoolean("logging.callback.cuts");
	
	final static int maxItersRoot = Settings.get().getInteger("cuts.iterations.root.max");
	final static int maxItersNodes = Settings.get().getInteger("cuts.iterations.nodes.max");
	
	final static int minCliques = Settings.get().getInteger("cuts.minCliques");
	final static int cutEvery = Settings.get().getInteger("cuts.everynodes");
	final static int maxCutsDepth = Settings.get().getInteger("cuts.maxdepth");
	final static boolean onlyOnUp = Settings.get().getBoolean("cuts.onlyonup");
	
	final static boolean enabled = Settings.get().getBoolean("cuts.enabled");
	private static final boolean runPrimalOnCutCallback = Settings.get().getBoolean("primal.runoncutcallback");
	
	Iteration iteration;
	Model model;
	IPartitionedGraph graph;
	IloMPModeler modeler;
	
	IBoundedAlgorithm cliques;
	IBoundedAlgorithm holes;
	IBoundedAlgorithm blocks;
	IBoundedAlgorithm gholes;
	
	CutsMetrics metrics;
	int iters = 0;
	int lastNnodes = -1;

	boolean doneFirst = false;
	boolean onlyRoot = false;
	
	double[] ws;
	double[][] xs;
	
	Double[] rootTimes;
	Double[] rootGaps;
	Double[] rootLBs;
	
	public CutCallback(IloMPModeler modeler, boolean onlyRoot) {
		this.modeler = modeler;
		this.metrics = new CutsMetrics();
		this.onlyRoot = onlyRoot;
		this.rootGaps = new Double[maxItersRoot+1];
		this.rootLBs = new Double[maxItersRoot+1];
		this.rootTimes = new Double[maxItersRoot+1];
	}
	
	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		if (logCallback) System.out.println("Cut callback on node " + super.getNnodes());
		
		// Skip first iteration of all as it takes place even before first relaxation is solved
		if (!doneFirst) {
			doneFirst = true;
			return;
		}

		// Reset iter count on node change
		if (lastNnodes != super.getNnodes()) {
			lastNnodes = super.getNnodes();
			clearPrimal();
			iters = 0;
		}	
	
		if (cut() && runPrimalOnCutCallback)
			primal();
	}

	private boolean cut() throws IloException {
	
		// Only cuts on initial node if specified
		if (onlyRoot && isInternalNode()) {
			return false;
		}
	
		// Do not add cuts on every node
		if (isInternalNode() && (
				(!onlyOnUp && super.getNnodes() % cutEvery != 0) || 
				(onlyOnUp && NodeData.getDirection(super.getNodeData()) != 1))) {
			if (logCallback) {
				if (!onlyOnUp) System.out.println("Skipping cuts for node " + super.getNnodes() + " not divisible by " + cutEvery);
				else System.out.println("Skipping cuts for node " + super.getNnodes() + " with direction " + NodeData.getDirection(super.getNodeData()));
			} return false;
		}
		
		// On certain depth, don't make any more cuts
		// TODO: Check max depth using only up branches
		if (isInternalNode() && maxCutsDepth > 0) {
			Integer depth = NodeData.getDepth(getNodeData());
			if (depth != null && maxCutsDepth < depth) {
				if (logCallback) System.out.println("Skipping cuts for node " + super.getNnodes() + " of depth " + depth);
				return false;
			}
		}
		
		// Log current gap on root only
		if (!isInternalNode()) {
			rootGaps[iters] = super.getMIPRelativeGap();
			rootLBs[iters] = super.getValue(model.getColorSum());
			//rootTimes[iters] //TODO: Fill root times
		}
		
		// Run a maximum number of times on each node
		int maxIters = super.getNnodes() == 0 ? maxItersRoot : maxItersNodes;
		if (maxIters > 0 && maxIters <= iters) {
			if (onlyRoot || logCallback) System.out.println("Ended cut callback execution after " + iters + " iterations.");
			return false;
		} metrics.newIter(); iters++;
		
		// Create whatever data is needed for this iteration
		setupIterationData();
	
		// Log what needs to be logged on current iteration
		logIteration();
		
		// Cut initial families
		if (logCallback) System.out.println("Initiating clique cuts");
		cliques = new ExtendedCliqueCutter(iteration.clone()).run();
	
		if (logCallback) System.out.println("Initiating block color cuts");
		blocks = new BlockColorCuts(iteration.clone()).run();
		
		// If a not good enough number of cuts is performed, try other families
		if (metrics.getCurrentIterCount(cliques, blocks) < minCliques) {
			if (!usePathsAlgorithm) {
				if (logCallback) System.out.println("Initiating holes and gholes cuts using greek algorithm");
				holes = new ComponentHolesCuts(iteration.clone()).run();
				gholes = new HolesCuts(iteration.clone()).run();
			} else {
				if (logCallback) System.out.println("Initiating holes and gholes cuts using path greedy algorithm");
				holes = new ComponentPathDetector(iteration.clone()).run();
				gholes = new SimplePathDetector(iteration.clone()).run();
			}
		}
		
		// Log running times
		metrics.setIterTime(cliques, holes, blocks, gholes);
		metrics.printIter();
		
		if (logCallback) System.out.println("Finished cuts iteration");
		
		// Free objects
		cliques = null;
		holes = null;
		blocks = null;
		gholes = null;
		
		return true;
	
	}

	@Override
	public void addPath(List<Node> path, int color) {
		int alpha = IntUtils.ceilhalf(path.size());
		addIndependentSet(path, color, alpha, CutFamily.Path);
	}

	@Override
	public void addHole(List<Node> nodes, int color) {
		int alpha = IntUtils.floorhalf(nodes.size());
		addIndependentSet(nodes, color, alpha, CutFamily.Hole);
	}

	@Override
	public void addGPrimeHole(List<pcp.entities.simple.Node> snodes, int color) {
		int alpha = IntUtils.floorhalf(snodes.size());
		List<Node> nodes = new ArrayList<Node>();
		for (pcp.entities.simple.Node sn : snodes) {
			for (Node n : graph.getNodes(sn)) {
				nodes.add(n);
			}
		}
		
		addIndependentSet(nodes, color, alpha, CutFamily.GPHole);
	}

	
	@Override
	public void addGPrimePath(List<pcp.entities.simple.Node> snodes, int color) {
		int alpha = IntUtils.ceilhalf(snodes.size());
		List<Node> nodes = new ArrayList<Node>();
		for (pcp.entities.simple.Node sn : snodes) {
			for (Node n : graph.getNodes(sn)) {
				nodes.add(n);
			}
		}
		
		addIndependentSet(nodes, color, alpha, CutFamily.GPPath);
	}

	@Override
	public void addClique(List<Node> nodes, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("CLIQUE[%1$d]", color);
			for (Node n : nodes) {
				expr.addTerm(model.x(n.index,color), 1);
			}
			
			expr.addTerm(model.w(color), -1);
			add(CutFamily.Clique, expr, LeqtZero, name);
			//System.out.println(super.getValue(model.w(color)));
			
		} catch (Exception ex) {
			System.err.println("Could not generate clique cut: " + ex.getMessage());
		}
		

	}
	
	@Override
	public void addBlockColor(Partition partition, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BLOCK[%1$d]", color);
			
			for (int j = color + 1; j < model.getColorCount(); j++) {
				for (Node node : graph.getNodes(partition)) {
					expr.addTerm(model.x(node.index, j), 1);
				}  
			} expr.addTerm(model.w(color), -1);
			
			add(CutFamily.BlockColor, expr, LeqtZero, name);
			
		} catch (Exception ex) {
			System.err.println("Could not generate block color cut: " + ex.getMessage());
		}
	}

	private void addIndependentSet(List<Node> nodes, int color, int alpha, CutFamily cut) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = cut.toString().toUpperCase() + String.format("[%1$d]", color);
			for (Node n : nodes) {
				expr.addTerm(model.x(n.index,color), 1);
			} expr.addTerm(model.w(color), -alpha);
			
			int basej = graph.P() - alpha;
		
			if (useBreakingSymmetry && color < basej && basej < model.getColorCount()) {
				for (Node n : graph.getNodes()) {
					for (int j = basej; j < model.getColorCount(); j++) {
						expr.addTerm(model.x(n.index,j), 1);
					}
				} expr.addTerm(model.w(basej), -1);
			}
			
			add(cut, expr, LeqtZero, name);
		} catch (Exception ex) {
			System.err.println("Could not generate independent set cut: " + ex.getMessage());
		}
	}

	private void add(CutFamily cut, IloNumExpr expr, boolean cmp, String name) throws IloException {
		IloRange range = cmp == LeqtZero 
			? modeler.le(expr, 0, name)
			: modeler.ge(expr, 0, name);
			
		if (local) {
			super.addLocal(range);
		} else {
			super.add(range);
		}
		
		metrics.added(cut, range);
		
		if (checkViolatedCut || logIneqs) {
			double val = super.getValue(expr);
			if (checkViolatedCut && ((val > -Constants.Epsilon && cmp == GeqtZero) || (val < Constants.Epsilon && cmp == LeqtZero))) {
				throw new IloException("Adding not violated cut: " + range.toString() + " (evals to " + val + ")");
			} else if (logIneqs) {
				System.out.println(cut.toString() + ": " + range.toString() + " VAL=" + val );
			}
		}
	}

	@Override
	public double w(int j) {
		return ws[j];
	}

	@Override
	public double[] ws() {
		return ws;
	}

	@Override
	public double x(int i, int j) {
		return xs[i][j];
	}

	@Override
	public double[] xs(int node) {
		return xs[node];
	}
	
	public void setModel(Model model) {
		this.model = model;
		this.graph = model.getGraph();
	}

	public CutsMetrics getMetrics() {
		return metrics;
	}

	public void fillData(Map<String, Object> data) {
		this.getMetrics().fillData(data);
		data.put("root.gaps", rootGaps);
		data.put("root.lbs", rootLBs);
		data.put("root.times", rootTimes);
	}

	private boolean isInternalNode() throws IloException {
		return super.getNnodes() > 0;
	}

	private void logIteration() throws IloException {
		if (logIterData || logIterColors) { 
			System.out.println("Current data:");
			IterationPrinter printer = new IterationPrinter(this, iteration.getModel().getGraph());
			if (logIterData) printer.printVerboseIteration();
			else printer.printColorsIteration();
		}
	}

	private void setupIterationData() {
		if (logCallback) {
			System.out.println("Setting up cuts iteration data");
		}
		try {
			ws = getValues(model.getWs());
			xs = new double[model.getNodeCount()][];
			for (int i = 0; i < xs.length; i++) {
				xs[i] = getValues(model.getXs()[i]);
			} 
		} catch (Exception ex) {
			System.err.println("Could not retrieve cplex data: " + ex.getMessage());
		} 
		
		this.iteration = new Iteration(model, this, this);
	}
	
//************************************************************************************
// DUPLICATE CODE FROM HEURISTIC CALLBACK
// Copied from heuristic callback to invoke primal heuristic here
//************************************************************************************
	
	private static final Coloring coloringStrategy = BuilderStrategy.fromSettings().getColoring();
	
	private static final boolean primalEnabled = Settings.get().getBoolean("primal.enabled");
	private static final boolean primalOnlyOnUp = Settings.get().getBoolean("primal.onlyonup");
	private static final double nodeLB = Settings.get().getDouble("primal.nodelb");
	private static final int everynodes = Settings.get().getInteger("primal.everynodes");
	
	private static final boolean validateSolutions = Settings.get().getBoolean("validate.heuristics");
	
	private static final DSaturAssignment assignment = Settings.get().getEnum("primal.dsatur.assign", DSaturAssignment.class);
	
	private HeuristicMetrics primalMetrics = new HeuristicMetrics();
	
	private double[] primalVals;
	private IloNumVar[] primalVars;
	private Integer primalChi; 
	
	private void primal() throws IloException {
		int nodesSet = countNodesEqualOne();
		if (!PruneEvaluator.shouldPrune(model, nodesSet) && primalEnabled && super.getNnodes() > 1 && (
				(!primalOnlyOnUp && super.getNnodes() % everynodes == 0) ||
				(primalOnlyOnUp && NodeData.getDirection(super.getNodeData()) == 1))) {
			setPrimal(nodesSet);
		}
	}
	
	public Integer getPrimalChi() {
		return primalChi;
	}
	
	public double[] getPrimalVals() {
		return primalVals;
	}
	
	public IloNumVar[] getPrimalVars() {
		return primalVars;
	}
	
	public void clearPrimal() {
		this.primalVals = null;
		this.primalVars = null;
		this.primalChi = null;
	}
	
	private void storeSolution(Integer chi, double[] vals, IloNumVar[] vars) {
		if (chi != null && (primalChi == null || primalChi > chi)) {
			if (logCallback) System.out.println("Storing solution in cut callback with chi " + chi);
			this.primalChi = chi;
			this.primalVals = vals;
			this.primalVars = vars;
		}
	}
	
	private void setPrimal(int nodesSet) {
		ColoringAlgorithm coloring = Factory.get()
			.coloring(coloringStrategy, graph)
			.withBounder(new SolutionsBounder("coloring.primal"));
		
		try {
			int fixed = assignColorsFromSolution(coloring);
			setLowerBound(coloring);
			createSolution(coloring);
			primalMetrics.primalHeur(coloring, super.getNnodes(), fixed, nodesSet);
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

		if (validateSolutions) validateSolution(vars, vals);
		storeSolution(chi, vals, vars);
	}
	
	private void validateSolution(IloNumVar[] vars, double[] vals) throws IloException, AlgorithmException {
		for (int i = 0; i < vars.length; i++) {
			IloNumVar var = vars[i];
			double d = vals[i];
			
			if (d < var.getLB() || d > var.getUB()) {
				throw new AlgorithmException("Invalid solution " + d + " for variable " + var + " bounds [" + var.getLB() + "," + var.getUB() + "] with relaxation value " + super.getValue(var));
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
