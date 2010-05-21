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
import pcp.model.Model;
import pcp.solver.cuts.CutFamily;
import pcp.solver.cuts.CutsMetrics;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.utils.IntUtils;
import pcp.utils.ModelUtils;
import props.Settings;


public class CutCallback extends IloCplex.CutCallback implements Comparisons, ICutBuilder, IModelData {

	static boolean checkViolatedCut = Settings.get().getBoolean("validate.cutsViolated");
	static boolean useBreakingSymmetry = Settings.get().getBoolean("cuts.iset.useBreakingSymmetry");
	static boolean usePathsAlgorithm = Settings.get().getBoolean("cuts.iset.usePathsAlgorithm");	
	static boolean logIterData = Settings.get().getBoolean("logging.iterData");
	static boolean logIneqs = Settings.get().getBoolean("logging.ineqs");
	
	static int maxItersRoot = Settings.get().getInteger("iterations.root.max");
	static int maxItersNodes = Settings.get().getInteger("iterations.nodes.max");
	
	static int minCliques = Settings.get().getInteger("cuts.minCliques");
	static int cutEvery = Settings.get().getInteger("cuts.everynodes");
	static int maxCutsDepth = Settings.get().getInteger("cuts.maxdepth");
	
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
	int maxIters;
	boolean doneFirst = false;
	boolean onlyRoot;
	
	double[] ws;
	double[][] xs;
	
	public CutCallback(IloMPModeler modeler, boolean onlyRoot) {
		this.modeler = modeler;
		this.metrics = new CutsMetrics();
		this.onlyRoot = onlyRoot;
		this.maxIters = onlyRoot ? maxItersRoot : maxItersNodes;
	}
	
	@Override
	protected void main() throws IloException {
		// Only cuts on initial node if specified
		if (onlyRoot && super.getNnodes() > 0) {
			if (doneFirst) return;
			doneFirst = true;
			return;
		}

		// Do not add cuts on every node
		if (super.getNnodes() > 0 && super.getNnodes() % cutEvery != 0) {
			return;
		}
		
		// On certain depth, don't make any more cuts
		if (maxCutsDepth < ModelUtils.countNodesFixed(super.getFeasibilities(model.getAllXs()), 
				super.getLBs(model.getAllXs()),
				super.getUBs(model.getAllXs()))) {
			return;
		}
		
		// Reset iter count on node change
		if (!onlyRoot && lastNnodes != super.getNnodes()) {
			lastNnodes = super.getNnodes();
			iters = 0;
		}
		
		// Run a maximum number of times on each node
		if (maxIters > 0 && maxIters <= iters) {
			if (onlyRoot || logIterData) System.out.println("Ended cut callback execution after " + iters + " iterations.");
			return;
		} metrics.newIter(); iters++;
		
		// Create whatever data is needed for this iteration
		setupIterationData();

		// Log what needs to be logged
		if (logIterData) { 
			System.out.println("Current data:");
			new IterationPrinter(this, iteration.getModel().getGraph()).printVerboseIteration();
			System.out.println();
		}
		
		// Cut initial families
		cliques = new ExtendedCliqueCutter(iteration.forAlgorithm()).run();
		blocks = new BlockColorCuts(iteration.forAlgorithm()).run();
		
		// If a not good enough number of cuts is performed, try other families
		if (metrics.getCurrentIterCount(cliques, blocks) < minCliques) {
			if (!usePathsAlgorithm) {
				holes = new ComponentHolesCuts(iteration.forAlgorithm()).run();
				gholes = new HolesCuts(iteration.forAlgorithm()).run();
			} else {
				holes = new ComponentPathDetector(iteration.forAlgorithm()).run();
				gholes = new SimplePathDetector(iteration.forAlgorithm()).run();
			}
			 
		}
		
		// Log running times
		metrics.setIterTime(cliques, holes, blocks, gholes);
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
			
		super.add(range);
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

	private void setupIterationData() {
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
