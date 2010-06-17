package pcp.solver;

import java.util.Map;

import exceptions.AlgorithmException;
import ilog.concert.IloException;
import pcp.model.Model;
import pcp.solver.callbacks.CutCallback;
import props.Settings;

public class PcpBranchAndCutSolver extends Solver {

	private final static boolean useCplexPreprocess = Settings.get().getBoolean("solver.useCplexPreprocess");
	private final static boolean useCutCallback = Settings.get().getBoolean("solver.useCutCallback");
	
	CutCallback callback;

	public PcpBranchAndCutSolver() throws IloException {
		this(false);
	}
	
	protected PcpBranchAndCutSolver(boolean cutsOnRootOnly) throws IloException {
		super();
		
		turnOffCplexCuts();
		turnOffDynamicSearch();
		
		if (!useCplexPreprocess) {
			turnOffPreprocess();
		}
		
		this.callback = new CutCallback(cplex, cutsOnRootOnly);
		if (useCutCallback) cplex.use(callback);
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
	protected void afterSolve() throws IloException, AlgorithmException {
		super.afterSolve();
		callback.getMetrics().printTotal();
	}
	
	@Override
	public void fillData(Map<String, Object> data) {
		super.fillData(data);
		callback.fillData(data);
	}
}
