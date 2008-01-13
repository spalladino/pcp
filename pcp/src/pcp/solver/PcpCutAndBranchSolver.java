package pcp.solver;

import pcp.model.Model;
import pcp.solver.callbacks.CutCallback;
import ilog.concert.IloException;

public class PcpCutAndBranchSolver extends BranchAndBoundSolver {

	CutCallback callback;

	public PcpCutAndBranchSolver() throws IloException {
		super();
		
		this.callback = new CutCallback(cplex);
		cplex.use(callback);
	}
	
	@Override
	public void setModel(Model model) {
		super.setModel(model);
		callback.setModel(model);
	}
}
