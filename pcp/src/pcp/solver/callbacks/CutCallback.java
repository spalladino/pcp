package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

import pcp.algorithms.block.BlockColorCuts;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.clique.ExtendedCliqueCutter;
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
import pcp.interfaces.IModelData;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Objective;
import pcp.solver.cuts.CutFamily;
import pcp.solver.cuts.CutsMetrics;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.utils.IntUtils;
import props.Settings;


public class CutCallback extends IloCplex.CutCallback implements Comparisons, ICutBuilder, IModelData {

	final static Objective objectiveStrategy = BuilderStrategy.fromSettings().getObjective();
	
	final static boolean checkViolatedCut = Settings.get().getBoolean("validate.cutsViolated");
	final static boolean useBreakingSymmetry = Settings.get().getBoolean("cuts.iset.useBreakingSymmetry");
	final static boolean usePathsAlgorithm = Settings.get().getBoolean("cuts.iset.usePathsAlgorithm");	
	final static boolean local = Settings.get().getBoolean("cuts.local");
	
	final static boolean logIterData = Settings.get().getBoolean("logging.iterData");
	final static boolean logIterColors = Settings.get().getBoolean("logging.iterColors");
	final static boolean logIneqs = Settings.get().getBoolean("logging.ineqs");
	final static boolean logCallback = Settings.get().getBoolean("logging.callback.cuts");
	
	final static int maxItersRoot = Settings.get().getInteger("iterations.root.max");
	final static int maxItersNodes = Settings.get().getInteger("iterations.nodes.max");
	
	final static int minCliques = Settings.get().getInteger("cuts.minCliques");
	final static int cutEvery = Settings.get().getInteger("cuts.everynodes");
	final static int maxCutsDepth = Settings.get().getInteger("cuts.maxdepth");
	
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
	
	public CutCallback(IloMPModeler modeler, boolean onlyRoot) {
		this.modeler = modeler;
		this.metrics = new CutsMetrics();
		this.onlyRoot = onlyRoot;
	}
	
	@Override
	protected void main() throws IloException {
		if (logCallback) System.out.println(("Node " + super.getNnodes()));
		
		// Skip first iteration of all as it takes place even before first relaxation is solved
		if (!doneFirst) {
			doneFirst = true;
			return;
		}
		
		// Only cuts on initial node if specified
		if (onlyRoot && isInternalNode()) {
			return;
		}

		// Do not add cuts on every node
		if (isInternalNode() && super.getNnodes() % cutEvery != 0) {
			if (logCallback) System.out.println("Skipping cuts for node " + super.getNnodes());
			return;
		}
		
		// On certain depth, don't make any more cuts
		if (isInternalNode() && maxCutsDepth > 0) {
			//final int countNodesFixed = ModelUtils.countNodesFixed(super.getFeasibilities(model.getAllXs()), super.getLBs(model.getAllXs()), super.getUBs(model.getAllXs()));
			Integer depth = (Integer)getNodeData();
			if (depth != null && maxCutsDepth < depth) {
				if (logCallback) System.out.println("Skipping cuts for node " + super.getNnodes() + " of depth " + depth);
				return;
			}
		}
		
		// Reset iter count on node change
		if (!onlyRoot && lastNnodes != super.getNnodes()) {
			lastNnodes = super.getNnodes();
			iters = 0;
		}
		
		// Run a maximum number of times on each node
		int maxIters = super.getNnodes() == 0 ? maxItersRoot : maxItersNodes;
		if (maxIters > 0 && maxIters <= iters) {
			if (onlyRoot || logCallback) System.out.println("Ended cut callback execution after " + iters + " iterations.");
			return;
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
				expr.addTerm(model.x(n.index(),color), 1);
			}
			
			expr.addTerm(model.w(color), -1);
			add(CutFamily.Clique, expr, LeqtZero, name);
			
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
					expr.addTerm(model.x(node.index(), j), 1);
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
				expr.addTerm(model.x(n.index(),color), 1);
			} expr.addTerm(model.w(color), -alpha);
			
			int basej = graph.P() - alpha;
		
			if (useBreakingSymmetry && color < basej && basej < model.getColorCount()) {
				for (Node n : graph.getNodes()) {
					for (int j = basej; j < model.getColorCount(); j++) {
						expr.addTerm(model.x(n.index(),j), 1);
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

}
