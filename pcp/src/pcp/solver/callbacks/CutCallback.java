package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.List;

import pcp.algorithms.clique.ExtendedCliqueDetector;
import pcp.algorithms.holes.ComponentHolesCuts;
import pcp.entities.Node;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.model.Model;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.utils.IntUtils;


public class CutCallback extends IloCplex.CutCallback implements ICutBuilder, IModelData {

	boolean logIneqs = false;
	boolean logIterData = false;
	
	Iteration iteration;
	Model model;
	IloMPModeler modeler;
	
	ExtendedCliqueDetector cliques;
	ComponentHolesCuts holes;
	
	double[] ws;
	double[][] xs;
	
	int cliqueCount = 0;
	int holeCount = 0;
	
	public CutCallback(IloMPModeler modeler) {
		this.modeler = modeler;
	}
	
	@Override
	protected void main() throws IloException {
		// Only cuts on initial node
		if (super.getNnodes() > 0) return;
		
		setupIterationData();
		
		if (logIterData) { 
			System.out.println("Current data:");
			new IterationPrinter(this, iteration.getModel().getGraph()).printVerboseIteration();
			System.out.println();
		}
		
		cliques = new ExtendedCliqueDetector(iteration).run();
		holes = new ComponentHolesCuts(iteration).run();
		
		System.out.println("Detected a total of " + cliqueCount + " cliques in " + cliques.getBounder().getMillis() + " millis.");
		System.out.println("Detected a total of " + holeCount + " holes in " + holes.getBounder().getMillis() + " millis.");
	}

	@Override
	public void addHole(List<Node> nodes, int color) {
		try {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("HOLE[%1$d]", color);
			for (Node n : nodes) {
				expr.addTerm(model.x(n.index(),color), 1);
			}
			
			expr.addTerm(model.w(color), -IntUtils.floorhalf(nodes.size()));
			IloRange range = modeler.le(expr, 0, name);
			add(range);
			holeCount++;
			
			if (logIneqs) {
				System.out.println(range.toString());
			}
			
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
			IloRange range = modeler.le(expr, 0, name);
			add(range);
			cliqueCount++;
			
			if (logIneqs) {
				System.out.println(range.toString());
			}
			
		} catch (Exception ex) {
			System.err.println("Could not generate clique cut: " + ex.getMessage());
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
