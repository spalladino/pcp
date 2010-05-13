package pcp.solver;

import java.util.Map;

import exceptions.AlgorithmException;
import ilog.concert.IloException;
import pcp.model.Model;
import pcp.solver.callbacks.CutCallback;

public class PcpBranchAndCutSolver extends Solver {

	CutCallback callback;

	public PcpBranchAndCutSolver(boolean cutsOnRootOnly) throws IloException {
		super();
		
		turnOffCplexCuts();
		turnOffDynamicSearch();
		turnOffPreprocess();
		
		//turnOffCplexPrimalHeur();
		
		this.callback = new CutCallback(cplex, cutsOnRootOnly);
		cplex.use(callback);
	}
	
	@Override
	public void setModel(Model model) {
		super.setModel(model);
		callback.setModel(model);
	}
	
	@Override
	protected void beforeSolve() throws IloException, AlgorithmException {
		super.beforeSolve();
		super.addInitialCuts();
		super.setBranchingSettings();
	}
	
	@Override
	public void fillExecutionData(Map<String, Object> data) throws Exception {
		super.fillExecutionData(data);
		callback.getMetrics().fillData(data);
	}
}
