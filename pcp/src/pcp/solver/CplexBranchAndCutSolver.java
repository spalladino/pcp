package pcp.solver;

import ilog.concert.IloException;

public class CplexBranchAndCutSolver extends Solver {

	public CplexBranchAndCutSolver() throws IloException {
		super();
		
		turnOffPreprocess();
		turnOffDynamicSearch();
	}

}
