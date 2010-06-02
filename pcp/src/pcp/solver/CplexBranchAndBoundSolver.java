package pcp.solver;

import ilog.concert.IloException;

public class CplexBranchAndBoundSolver extends Solver {

	public CplexBranchAndBoundSolver() throws IloException {
		super();
		
		turnOffCplexCuts();
		turnOffDynamicSearch();
	}

}
