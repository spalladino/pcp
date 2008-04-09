package pcp.solver.callbacks;

import pcp.algorithms.coloring.DSaturColoring;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.entities.IPartitionedGraph;
import pcp.model.Model;
import ilog.concert.IloException;
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
		try {
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
			
			super.setBounds(vars, vals, vals);
			super.solve();

		} catch (Exception ex) {
			System.err.println("Exception in heuristic callback: " + ex.toString());
		}
	}
	
}
