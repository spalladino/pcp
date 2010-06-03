package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.BranchDirection;
import ilog.cplex.IloCplex.BranchType;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;
import pcp.Logger;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.interfaces.IColorAssigner;
import pcp.model.Model;
import pcp.solver.helpers.NodeSaturations;
import pcp.solver.helpers.PruneEvaluator;
import pcp.utils.DoubleUtils;
import pcp.utils.ModelUtils;
import props.Settings;
import exceptions.AlgorithmException;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback {

	static final boolean log = Settings.get().getBoolean("logging.callback.branching");
	static final boolean enabled = Settings.get().getBoolean("callback.branching.enabled");
	static final boolean dynamicFractionalStrategy = Settings.get().getBoolean("branch.dynamic.fractional");
	static final boolean dynamicDSaturStrategy = Settings.get().getBoolean("branch.dynamic.dsatur");
	
	static final double nodeLB = Settings.get().getDouble("branch.dynamic.dsatur.nodelb");
	
	static final boolean manual = false;
	
	Model model;
	IPartitionedGraph graph;
	
	public BranchCallback(Model model) {
		this.model = model;
		this.graph = model.getGraph();
	}

	@Override
	protected void main() throws IloException {
		if (!enabled) return;
		int nodesSet = countNodesEqualOne(); 
		
		if (new PruneEvaluator(model).shouldPrune(nodesSet)) {
			if (log) System.out.println("Pruning at " + nodesSet + " nodes set");
			prune(); return;
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
		
		IloNumVar branched = null;
		
		if (super.getNbranches() > 0 && getBranchType().equals(BranchType.BranchOnVariable)) {
			if (dynamicFractionalStrategy) {
				branched = fractionalBranch();
			} else if (dynamicDSaturStrategy) {
				branched = dsaturBranch();
			}
		}
		
		if (branched != null) {
			if (log) System.out.println("Branching on " + branched);
        	super.makeBranch(branched, getValue(branched), BranchDirection.Up, getObjValue());
        	super.makeBranch(branched, getValue(branched), BranchDirection.Down, getObjValue());
        }
		
	}

	private IloNumVar dsaturBranch() throws IloException {
		NodeSaturations saturs = new NodeSaturations(graph);

		try {
			assignColorsFromSolution(saturs);
		} catch (Exception ex) {
			Logger.error("Error assigning coloring from solution in dsatur branching", ex);
			return null;
		}
		
		int bestNode = 0;
		int bestSatur = 0;
		int bestPrio = 0;
		
		//Pick most saturated, highest prio node
		for (int i = 0; i < graph.N(); i++) {
			IloIntVar var = model.x(i, 0);
			if (getFeasibility(var).equals(IntegerFeasibilityStatus.Infeasible)) {
				int satur = saturs.getSaturation(i);
				int prio = super.getPriority(var);
				if ((bestSatur < satur) || (bestSatur == satur && bestPrio < prio)) {
					bestNode = i;
					bestSatur = satur;
					bestPrio = prio;
				}
			}
		}
		
		// Branch on highest value color
		double bestVal = 0.0;
		IloNumVar branched = null;
		
		for (int j = 0; j < model.getColorCount(); j++) {
			IloNumVar var = model.x(bestNode, j);
			double val = getValue(var);
			if (val > bestVal) {
				val = bestVal;
				branched = var;
			}
		}
		
		return branched;
	}
	
	private int assignColorsFromSolution(IColorAssigner coloring) throws IloException, AlgorithmException {
		int fixedCount = 0;
		boolean[] colored = new boolean[graph.P()];
		
		// Iterate over nodes and colors checking for highest value among neighbours
		for (Node node : graph.getNodes()) {
			
			// Paint each partition only once regardless of the model
			if (colored[node.getPartition().index()]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				
				// Check if value is high enough to pass the lower bound
				double val = super.getValue(model.x(node.index(), j));
				if (val < nodeLB) continue;
				boolean isCandidate = true;
				
				// Check if it has the highest value among neighbours
				for (Node adj : graph.getNeighbours(node)) {
					if (val <= super.getValue(model.x(adj.index(), j))
					|| (val == super.getValue(model.x(adj.index(), j)) && node.index() > adj.index())) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Same for copartition
				for (Node adj : graph.getNodes(node.getPartition())) {
					if (adj.index() != node.index() && 
						((val <= super.getValue(model.x(adj.index(), j)))
						|| (val == super.getValue(model.x(adj.index(), j)) && node.index() > adj.index()))) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Use that color for the node
				coloring.useColor(node.index(), j);
				colored[node.getPartition().index()] = true;
				fixedCount++;
				break;
			}
		}
		
		return fixedCount;
	}


	private IloNumVar fractionalBranch() throws IloException {
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
				if (branched == null 
					|| frac > mostFrac + 0.1 
					|| (frac > mostFrac - 0.1 && prio > bestPrio)
					|| (frac > mostFrac && bestPrio == 0)) {
					
					branched = vars[i];
					bestPrio = prio;
					mostFrac = frac;
				}
			}
		}
		return branched;
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);

	}
	
}
