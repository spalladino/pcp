package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.BranchDirection;
import ilog.cplex.IloCplex.BranchType;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.model.Model;
import pcp.utils.DoubleUtils;
import pcp.utils.ModelUtils;
import props.Settings;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback {

	static final boolean log = Settings.get().getBoolean("logging.callback.branching");
	static final boolean enabled = Settings.get().getBoolean("callback.branching.enabled");
	static final boolean dynamicStrategy = Settings.get().getBoolean("branch.dynamic");
	static final boolean manual = false;
	
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

		if (manual) {
			IloNumVar[][] varss = new IloNumVar[super.getNbranches()][];
			double[][] bounds = new double[super.getNbranches()][];
			BranchDirection[][] dirs = new BranchDirection[super.getNbranches()][];
			double[] branches = super.getBranches(varss, bounds, dirs);
			Integer depth = (Integer) super.getNodeData();
			depth = depth == null ? 1 : depth + 1;
			for (int i = 0; i < super.getNbranches(); i++) {
				super.makeBranch(varss[i], bounds[i], dirs[i], branches[i], depth);
			}
		}
		
		if (dynamicStrategy 
			&& super.getNbranches() > 0 
			&& getBranchType().equals(BranchType.BranchOnVariable)) {
			
			IloNumVar[] vars = model.getAllXs();
			double[] values = getValues(vars);
	        IntegerFeasibilityStatus[] feasibilities = getFeasibilities(vars);
			
	        IloNumVar branched = null;
	        int bestPrio = 0;
	        double mostFrac = 0.0;
	        
	        for (int i = 0; i < vars.length; i++) {
	        	if (feasibilities[i].equals(IntegerFeasibilityStatus.Infeasible)) {
	        		double frac = DoubleUtils.fractionality(values[i]);
	        		int prio = getPriority(vars[i]);
	        		
	        		// Branch on most fractional?
	        		if (branched == null || frac > mostFrac + 0.2 || (frac > mostFrac - 0.2 && prio > bestPrio)) {
	        			branched = vars[i];
	        			bestPrio = prio;
	        			mostFrac = frac;
	        		}
	        	}
	        }
	        
	        if (branched != null) {
	        	super.makeBranch(branched, getValue(branched), BranchDirection.Up, getObjValue());
	        	super.makeBranch(branched, getValue(branched), BranchDirection.Down, getObjValue());
	        }
		}
		
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);

	}
	
}
