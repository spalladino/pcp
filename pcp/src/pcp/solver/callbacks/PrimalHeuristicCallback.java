package pcp.solver.callbacks;

import pcp.algorithms.coloring.DSaturColoring;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.entities.IPartitionedGraph;
import pcp.model.Model;
import pcp.utils.DoubleUtils;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;


public class PrimalHeuristicCallback extends ilog.cplex.IloCplex.HeuristicCallback {

	IPartitionedGraph graph;
	Model model;
	
	public PrimalHeuristicCallback() {
	
	}

	public PrimalHeuristicCallback(Model model) {
		this.graph = model.getGraph();
		this.model = model;
	}

	
	@Override
	protected void main() throws IloException {
		// TODO: Use current solution information, and run completely only if depth is more than X
		
		try {
			
			int count = countVarsSet();
			
			System.out.println("Equals: " + count);
			
			/*
			DSaturPartitionColoringEasiestNodes coloring = new DSaturPartitionColoringEasiestNodes(graph);

			int n = model.getNodeCount();
			int c = model.getColorCount();
			int chi = coloring.getChi();
			
			double[] vals = new double[n*c + c];
			IloNumVar[] vars = new IloNumVar[n*c + c];
			
			int idx = 0;
			for (int j = 0; j < c; j++) {
				// Set w variable value
				vars[idx] = model.getWs()[j];
				vals[idx] = j < chi ? 1 : 0; 
				idx++;
				for (int i = 0; i < n; i++) {
					// Set x variable value
					vars[idx] = model.getXs()[i][j];
					vals[idx] = coloring.getColor(i) == j ? 1 : 0;
					idx++;
				}
			}
			
			System.out.println("Coloring: " + coloring.getChi());
			
			super.setSolution(vars, vals);
			super.solve();
			*/

		} catch (Exception ex) {
			System.err.println("Exception in heuristic callback: " + ex.toString());
		}
	}

	private int countVarsSet() throws IloException {
		int count = 0;
		for (int j = 0; j < model.getColorCount(); j++) {
			IloIntVar w = model.w(j);
			if (super.getLB(w) == super.getUB(w)) count++;
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i,j);
				if (super.getLB(x) == super.getUB(x)) count++;
			}	
		}
		return count;
	}
	
}
