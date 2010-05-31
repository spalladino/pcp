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
	static final double pruningFrac = Settings.get().getDouble("pruning.frac");
	
	Model model;
	
	public BranchCallback(Model model) {
		this.model = model;
	}

	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		int nodesSet = countNodesEqualOne(); 
		
		if (nodesSet >= (model.getGraph().P() - pruningRemaining)
			|| pruningFrac <= ((double)nodesSet / (double)model.getGraph().P())) {
			if (log) System.out.println("Pruning at " + nodesSet + " nodes set");
			prune();
			return;
		}

//		IloNumVar[][] vars = new IloNumVar[super.getNbranches()][];
//		double[][] bounds = new double[super.getNbranches()][];
//		BranchDirection[][] dirs = new BranchDirection[super.getNbranches()][];
//		double[] branches = super.getBranches(vars, bounds, dirs);
//		
//		for (int i = 0; i < super.getNbranches(); i++) {
//			super.makeBranch(vars[i], bounds[i], dirs[i], branches[i]);
//		}
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);

	}
	
}
