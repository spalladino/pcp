package pcp.solver.branching;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.IntParam;
import pcp.model.Model;
import props.Settings;


public class BranchingDirectioner {

	final static int branchDirection = Settings.get().getInteger("branch.direction");
	
	IloCplex cplex; 
	Model model;
	
	public BranchingDirectioner(IloCplex cplex, Model model) {
		this.cplex = cplex;
		this.model = model;
	}

	public void setDirection() throws IloException {
		if (branchDirection != 0) {
			cplex.setParam(IntParam.BrDir, branchDirection);
		} 
	}
}
