package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.cplex.IloCplex;

import java.util.List;

import pcp.algorithms.clique.ExtendedCliqueDetector;
import pcp.entities.Node;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.interfaces.ISolutionData;
import pcp.model.Model;
import pcp.solver.data.Iteration;
import pcp.solver.io.IterationPrinter;
import pcp.solver.io.Printer;


public class CutCallback extends IloCplex.CutCallback implements ICutBuilder, IModelData {

	boolean logIneqs = true;
	
	Iteration iteration;
	Model model;
	IloMPModeler modeler;
	
	ExtendedCliqueDetector cliques;
	
	double[] ws;
	double[][] xs;
	
	int cliqueCount = 0;
	
	public CutCallback(IloMPModeler modeler) {
		this.modeler = modeler;
	}
	
	@Override
	protected void main() throws IloException {
		// Only cuts on initial node
		if (super.getNnodes() > 0) return;
		
		setupIterationData();
		
		System.out.println("Current data:");
		new IterationPrinter(this, iteration.getModel().getGraph()).printVerboseIteration();
		System.out.println();
		
		cliques = new ExtendedCliqueDetector(iteration);
		cliques.run();
		
		System.out.println("Detected " + cliqueCount + " cliques in " + cliques.getBounder().getMillis() + " millis.");
	}

	@Override
	public void addClique(List<Node> nodes, int color) {
		try {
			
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("CLIQUE[%1$d]", color);
			String values = "";
			for (Node n : nodes) {
				expr.addTerm(model.x(n.index(),color), 1);
				values += String.valueOf(this.x(n.index(), color)) + " + ";
			}
			
			expr.addTerm(model.w(color), -1);
			values += "-" + String.valueOf(this.w(color)) + " <= 0";
			modeler.addLe(expr, 0, name);
			cliqueCount++;
			
			if (logIneqs) {
				System.out.println(expr.toString());
				System.out.println(values);
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
