package pcp.solver;

import ilog.concert.IloException;


public class PcpCutAndBranchSolver extends PcpBranchAndCutSolver {

	public PcpCutAndBranchSolver() throws IloException {
		super(true);
	}
	
}
