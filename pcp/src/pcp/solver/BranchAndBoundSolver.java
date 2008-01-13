package pcp.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.BooleanParam;
import ilog.cplex.IloCplex.IntParam;

public class BranchAndBoundSolver extends Solver {

	public BranchAndBoundSolver() throws IloException {
		super();
		
        cplex.setParam(IntParam.Reduce, 0);
        cplex.setParam(BooleanParam.PreLinear, false);
        cplex.setParam(IntParam.HeurFreq, -1);
        cplex.setParam(IntParam.AggInd, 0);
        cplex.setParam(BooleanParam.PreInd, false);
        cplex.setParam(IntParam.PrePass, 0);
        cplex.setParam(IntParam.RelaxPreInd, 0);
        cplex.setParam(IntParam.PreDual, -1);
        cplex.setParam(IntParam.Cliques, -1);
        cplex.setParam(IntParam.Covers, -1);
        cplex.setParam(IntParam.DisjCuts, -1);
        cplex.setParam(IntParam.FlowCovers, -1);
        cplex.setParam(IntParam.FlowPaths, -1);
        cplex.setParam(IntParam.FracCuts, -1);
        cplex.setParam(IntParam.GUBCovers, -1);
        cplex.setParam(IntParam.ImplBd, -1);
        cplex.setParam(IntParam.MIRCuts, -1);
        cplex.setParam(IntParam.AggCutLim, 0);
        cplex.setParam(IntParam.ZeroHalfCuts, -1);
	}
	
	

}
