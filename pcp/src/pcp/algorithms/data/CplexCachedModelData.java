package pcp.algorithms.data;

import pcp.model.Model;
import ilog.cplex.IloCplex;


public class CplexCachedModelData extends ModelData {
	
	public CplexCachedModelData(IloCplex cplex, Model model) {
		try {
			ws = cplex.getValues(model.getWs());
			xs = new double[model.getNodeCount()][];
			for (int i = 0; i < xs.length; i++) {
				xs[i] = cplex.getValues(model.getXs()[i]);
			}
		} catch (Exception ex) {
			System.err.println("Could not retrieve cplex data: " + ex.getMessage());
		}
	}
	
}
