package pcp.solver;

import ilog.concert.IloException;

public class CplexDynamicSearchSolver extends Solver {

	public CplexDynamicSearchSolver() throws IloException {
		super();
		turnOffPreprocess();
	}

}
