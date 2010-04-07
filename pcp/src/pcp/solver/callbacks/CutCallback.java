package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.List;

import pcp.algorithms.block.BlockColorCuts;
import pcp.algorithms.clique.ExtendedCliqueCutter;
import pcp.algorithms.clique.ExtendedCliqueDetector;
import pcp.algorithms.holes.ComponentHolesCuts;
import pcp.algorithms.holes.HolesCuts;
import pcp.definitions.Comparisons;
import pcp.definitions.Constants;
import pcp.definitions.Cuts;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.metrics.CutsMetrics;
import pcp.model.Model;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.utils.IntUtils;
import props.Settings;


public class CutCallback extends IloCplex.CutCallback implements Comparisons, Cuts, ICutBuilder, IModelData {

	static boolean checkViolatedCut = Settings.get().getBoolean("validate.cutsViolated");
	static boolean useBreakingSymmetry = Settings.get().getBoolean("holes.useBreakingSymmetry");
	
	static boolean logIterData = Settings.get().getBoolean("logging.iterData");
	static boolean logIneqs = Settings.get().getBoolean("logging.ineqs");
	
	static int maxIters = Settings.get().getInteger("iterations.max");
	
	Iteration iteration;
	Model model;
	IPartitionedGraph graph;
	IloMPModeler modeler;
	
	ExtendedCliqueDetector cliques;
	ComponentHolesCuts holes;
	BlockColorCuts blocks;
	HolesCuts gholes;
	
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
		metrics.newIter();
		if (maxIters > 0 && maxIters < metrics.getNIters()) {
			System.out.println("Finishing callback execution after " + metrics.getNIters() + " iterations.");
			return;
		}
		
		setupIterationData();

		// Log what needs to be logged
		if (logIterData) { 
			System.out.println("Current data:");
			new IterationPrinter(this, iteration.getModel().getGraph()).printVerboseIteration();
			System.out.println();
		}
		
		// And cut!
		cliques = new ExtendedCliqueCutter(iteration.forAlgorithm()).run();
		holes = new ComponentHolesCuts(iteration.forAlgorithm()).run();
		blocks = new BlockColorCuts(iteration.forAlgorithm()).run();
		gholes = new HolesCuts(iteration.forAlgorithm()).run();
		
		metrics.iterTime(cliques, holes, blocks, gholes);
	}

	private void addIndependentSet(List<Node> nodes, int color, int clazz) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("HOLE[%1$d]", color);
			int alpha = IntUtils.floorhalf(nodes.size());
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
			
			add(Holes, expr, LeqtZero, name);
			
		} catch (Exception ex) {
			System.err.println("Could not generate hole cut: " + ex.getMessage());
		}
	}
	
	@Override
	public void addPath(List<Node> path, int color) {
		// TODO Auto-generated method stub
		
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
				for (Node n : graph.getNodes()) {
					for (int j = basej; j < model.getColorCount(); j++) {
						expr.addTerm(model.x(n.index(),j), 1);
					}
				} expr.addTerm(model.w(basej), -1);
			}
			
			add(Holes, expr, LeqtZero, name);
			
		} catch (Exception ex) {
			System.err.println("Could not generate hole cut: " + ex.getMessage());
		}
	}

	@Override
	public void addGPrimeHole(List<pcp.entities.simple.Node> nodes, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("HOLE[%1$d]", color);
			int alpha = IntUtils.floorhalf(nodes.size());
			for (pcp.entities.simple.Node sn : nodes) {
				for (Node n : graph.getNodes(sn)) {
					expr.addTerm(model.x(n.index(),color), 1);
				}
			} expr.addTerm(model.w(color), -alpha);
			
			int basej = graph.P() - alpha;
			
			if (useBreakingSymmetry && color < basej && basej < model.getColorCount()) {
				for (Node n : graph.getNodes()) {
					for (int j = basej; j < model.getColorCount(); j++) {
						expr.addTerm(model.x(n.index(),j), 1);
					}
				} expr.addTerm(model.w(basej), -1);
			}
			
			add(GPrimeHoles, expr, LeqtZero, name);
			
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

	public CutsMetrics getMetrics() {
		return metrics;
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
				throw new IloException("Adding not violated cut: " + range.toString() + " (evals to " + val + ")");
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
