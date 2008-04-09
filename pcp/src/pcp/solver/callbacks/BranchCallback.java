package pcp.solver.callbacks;

import pcp.definitions.Constants;
import ilog.concert.IloException;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback {

	int counts = 0;
	
	@Override
	protected void main() throws IloException {
		// TODO: Invoke getBranches and set them to see if performance is unaffected
		
		if (counts++ > 1000000) {
			System.out.println("Pruning!");
			prune();
		} else {
			System.out.println("Not pruning");
		}
	}
	
}
