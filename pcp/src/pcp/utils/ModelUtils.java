package pcp.utils;

import ilog.cplex.IloCplex.IntegerFeasibilityStatus;

public class ModelUtils {

	public static int countNodesFixedToOne(IntegerFeasibilityStatus[] feasibilities, double[] lbs) {
		int count = 0;
		for (int i = 0; i < feasibilities.length; i++) {
			if (feasibilities[i] != IntegerFeasibilityStatus.Infeasible && lbs[i] > 0.99) {
				count++;
			}
		} return count;
	}
	
	public static int countNodesFixed(IntegerFeasibilityStatus[] feasibilities, double[] lbs, double[] ubs) {
		int count = 0;
		for (int i = 0; i < feasibilities.length; i++) {
			if (feasibilities[i] != IntegerFeasibilityStatus.Infeasible 
					&& (lbs[i] == 1 || ubs[i] == 0)) {
				count++;
			}
		} return count;
	}
	
}
