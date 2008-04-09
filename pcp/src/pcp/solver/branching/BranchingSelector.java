package pcp.solver.branching;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.IntParam;
import pcp.model.Model;
import props.Settings;


public class BranchingSelector {

	final static int branchSelection = Settings.get().getInteger("branch.selection");
	
	IloCplex cplex; 
	Model model;
	
	public BranchingSelector(IloCplex cplex, Model model) {
		this.cplex = cplex;
		this.model = model;
	}

	public void setSelection() throws IloException {
		if (branchSelection != 0) {
			cplex.setParam(IntParam.NodeSel, branchSelection);
		} 
	}
}
