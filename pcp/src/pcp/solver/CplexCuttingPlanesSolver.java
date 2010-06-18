package pcp.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;

public class CplexCuttingPlanesSolver extends CplexBranchAndCutSolver {

	public CplexCuttingPlanesSolver() throws IloException {
		super();
		cplex.setParam(IntParam.NodeLim, 0);
	}

}
