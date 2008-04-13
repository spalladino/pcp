package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import pcp.model.Model;
import props.Settings;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback {

	static final boolean log = Settings.get().getBoolean("logging.callback.branching");
	static final boolean enabled = Settings.get().getBoolean("callback.branching.enabled");
	static final int pruningRemaining = Settings.get().getInteger("callback.pruning.remaining");
	
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
	}
	
	private int countNodesEqualOne() throws IloException {
		int count = 0;
		for (int j = 0; j < model.getColorCount(); j++) {
			for (int i = 0; i < model.getNodeCount(); i++) {
				IloIntVar x = model.x(i,j);
				if (super.getLB(x) > 0.99) count++;
			}	
		}
		return count;
	}
	
}
