package pcp.solver.branching;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;
import pcp.model.Model;
import props.Settings;


public class BranchingPrioritizer extends BasePrioritizer {
	
	final static boolean enabled = Settings.get().getBoolean("branch.prios.enabled");

	IloCplex cplex;
	Model model;
	
	public BranchingPrioritizer(IloCplex cplex, Model model) {
		super(model.getGraph(), model.getColorCount());
		this.cplex = cplex;
		this.model = model;
	}
	
	public BranchingPrioritizer setPriorities() throws IloException {
		if (!enabled) return this;
		
		for (int i = 0; i < model.getNodeCount(); i++) {
			setNodePriorities(i);
		}
		
		return this;
	}

	private void setNodePriorities(int i) throws IloException {
		int nodeprio = getNodePriority(i);
		for (int j = 0; j < model.getColorCount(); j++) {
			int prio = nodeprio + getColorPriority(j);
			setPriority(i, j, prio);
		}
	}
	
	protected void setPriority(int i, int j, int prio) throws IloException {
		IloIntVar var = model.x(i, j);
		cplex.setPriority(var, prio);
	}
	
	
}
