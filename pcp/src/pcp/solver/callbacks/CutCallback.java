package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.List;

import pcp.Settings;
import pcp.algorithms.block.BlockColorCuts;
import pcp.algorithms.clique.ExtendedCliqueCuts;
import pcp.algorithms.holes.ComponentHolesCuts;
import pcp.definitions.Comparisons;
import pcp.definitions.Constants;
import pcp.definitions.Cuts;
import pcp.entities.Node;
import pcp.entities.Partition;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.interfaces.IPartitionedGraph;
import pcp.metrics.CutsMetrics;
import pcp.model.Model;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.utils.IntUtils;


public class CutCallback extends IloCplex.CutCallback implements Comparisons, Cuts, ICutBuilder, IModelData {

	static boolean checkViolatedCut = Settings.get().getBoolean("validate.checkViolatedCut");
	static boolean useBreakingSymmetry = Settings.get().getBoolean("holes.useBreakingSymmetry");
	static boolean allowSkipBreakingSymmetry = Settings.get().getBoolean("holes.allowSkipBreakingSymmetry");
	
	static boolean logIterData = Settings.get().getBoolean("logging.iterData");
	static boolean logIneqs = Settings.get().getBoolean("logging.ineqs");
	
	static int maxIters = Settings.get().getInteger("iterations.max");
	
	Iteration iteration;
	Model model;
	IPartitionedGraph graph;
	IloMPModeler modeler;
	
	ExtendedCliqueCuts cliques;
	ComponentHolesCuts holes;
	BlockColorCuts blocks;
	
	CutsMetrics metrics;
	boolean doneFirst = false;
	
	double[] ws;
	double[][] xs;
	
	public CutCallback(IloMPModeler modeler) {
		this.modeler = modeler;
		this.metrics = new CutsMetrics();
	}
	
	@Override
	protected void main() throws IloException {
		// Only cuts on initial node
		if (super.getNnodes() > 0) {
			if (doneFirst) return;
			metrics.printTotal();
			doneFirst = true;
			return;
		}
		
		// Run a maximum number of times on the root
		if (maxIters > 0 && maxIters < metrics.getNIters()) {
			System.out.println("Finishing callback execution after " + metrics.getNIters() + " iterations.");
			return;
		}
		
		setupIterationData();
		metrics.newIter();
		
		// Log what needs to be logged
		if (logIterData) { 
			System.out.println("Current data:");
			new IterationPrinter(this, iteration.getModel().getGraph()).printVerboseIteration();
			System.out.println();
		}
		
		// And cut!
		cliques = new ExtendedCliqueCuts(iteration.forAlgorithm()).run();
		holes = new ComponentHolesCuts(iteration.forAlgorithm()).run();
		blocks = new BlockColorCuts(iteration.forAlgorithm()).run();
		
		metrics.iterTime(cliques, holes, blocks);
	}

	@Override
	public void addHole(List<Node> nodes, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("HOLE[%1$d]", color);
			int alpha = IntUtils.floorhalf(nodes.size());
			for (Node n : nodes) {
				expr.addTerm(model.x(n.index(),color), 1);
			} expr.addTerm(model.w(color), -alpha);
			
			int basej = graph.P() - alpha;
			
			if (useBreakingSymmetry && color < basej && basej < model.getColorCount()) {
				// TODO: Check ineq ok
				for (Node n : graph.getNodes()) {
					for (int j = basej; j < model.getColorCount(); j++) {
						expr.addTerm(model.x(n.index(),j), 1);
					}
				} expr.addTerm(model.w(basej), -1);
			}
		
			if (allowSkipBreakingSymmetry && useBreakingSymmetry && super.getValue(expr) < Constants.Epsilon) {
				System.out.println("Falling back to simple constraint");
				expr.clear();
				for (Node n : nodes) {
					expr.addTerm(model.x(n.index(),color), 1);
				} expr.addTerm(model.w(color), -alpha);
			}
			
			add(Holes, expr, LeqtZero, name);
			
		} catch (Exception ex) {
			System.err.println("Could not generate hole cut: " + ex.getMessage());
		}
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
			add(Cliques, expr, LeqtZero, name);
			
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
				for (Node node : partition.getNodes()) {
					expr.addTerm(model.x(node.index(), j), 1);
				}  
			} expr.addTerm(model.w(color), -1);
			
			add(BlockColor, expr, LeqtZero, name);
			
		} catch (Exception ex) {
			System.err.println("Could not generate block color cut: " + ex.getMessage());
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

	private void add(int cut, IloNumExpr expr, boolean cmp, String name) throws IloException {
		IloRange range = cmp == LeqtZero 
			? modeler.le(expr, 0, name)
			: modeler.ge(expr, 0, name);
			
		super.add(range);
		metrics.added(cut, range);
		
		if (checkViolatedCut || logIneqs) {
			double val = super.getValue(expr);
			if (checkViolatedCut && ((val > -Constants.Epsilon && cmp == GeqtZero) || (val < Constants.Epsilon && cmp == LeqtZero))) {
				System.err.println("Adding not violated cut: " + range.toString() + " (evals to " + val + ")");
			} else if (logIneqs) {
				System.out.println(Names[cut] + ": " + range.toString() + " VAL=" + val );
			}
		}
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
