package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.model.Model;
import pcp.utils.ModelUtils;
import props.Settings;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback {

	static final boolean log = Settings.get().getBoolean("logging.callback.branching");
	static final boolean enabled = Settings.get().getBoolean("callback.branching.enabled");
	static final int pruningRemaining = Settings.get().getInteger("pruning.remaining");
	
	Model model;
	
	public BranchCallback(Model model) {
		this.model = model;
	}

	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		if (countNodesEqualOne() >= (model.getGraph().P() - pruningRemaining)) {
			if (log) System.out.println("Pruning at " + countNodesEqualOne() + " nodes set");
			prune();
		}

		// TODO: Manually do cplex suggested branches in order to generate node data with 1s count, depth, etc
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);

	}
	
}
