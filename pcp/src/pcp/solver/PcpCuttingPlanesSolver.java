package pcp.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntParam;


public class PcpCuttingPlanesSolver extends PcpCutAndBranchSolver {

	public PcpCuttingPlanesSolver() throws IloException {
		super();
		cplex.setParam(IntParam.NodeLim, 0);
	}
	
}
