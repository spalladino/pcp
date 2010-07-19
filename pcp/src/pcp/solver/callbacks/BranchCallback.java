package pcp.solver.callbacks;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.BranchDirection;
import ilog.cplex.IloCplex.BranchType;
import ilog.cplex.IloCplex.IntegerFeasibilityStatus;

import java.util.Arrays;
import java.util.Map;

import pcp.Logger;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.IColorAssigner;
import pcp.interfaces.IColorAssigner.DSaturAssignment;
import pcp.interfaces.IExecutionDataProvider;
import pcp.model.BuilderStrategy;
import pcp.model.Model;
import pcp.model.strategy.Objective;
import pcp.solver.data.NodeData;
import pcp.solver.helpers.NodeSaturations;
import pcp.solver.helpers.PruneEvaluator;
import pcp.utils.DoubleUtils;
import pcp.utils.ModelUtils;
import props.Settings;
import entities.TupleInt;
import exceptions.AlgorithmException;


public class BranchCallback extends ilog.cplex.IloCplex.BranchCallback implements IExecutionDataProvider {
	
	private static final Objective objectiveStrategy = BuilderStrategy.fromSettings().getObjective();
	
	private static final boolean log = Settings.get().getBoolean("logging.callback.branching");
	private static final boolean enabled = Settings.get().getBoolean("branch.enabled");

	private static final boolean dynamicFractionalStrategy = Settings.get().getBoolean("branch.dynamic.fractional");
	private static final boolean mostFrac = Settings.get().getBoolean("branch.dynamic.fractional.most");
	private static final double fracTol = 0.05;
	private static final double branchLB = 0.1;
	
	private static final boolean dynamicDSaturStrategy = Settings.get().getBoolean("branch.dynamic.dsatur");
	private static final double nodeLB = Settings.get().getDouble("branch.dynamic.dsatur.nodelb");
	private static final boolean consecColors = Settings.get().getBoolean("branch.dynamic.dsatur.conseccolors");
	private static final DSaturAssignment assignment = Settings.get().getEnum("branch.dsatur.assign", DSaturAssignment.class);
	
	private static final boolean branchSingle = Settings.get().getBoolean("branch.singlevar");
	private static final boolean boundWs = Settings.get().getBoolean("branch.boundws");
	
	private static final boolean manual = true;
	
	Model model;
	IPartitionedGraph graph;
	
	IloNumVar branched;
	int branchedNode;
	int branchedColor;
	
	int lastColorFixed = 0;
	
	double lastGap = Double.MAX_VALUE;
	int lastGapNode = 0;
	
	public BranchCallback(Model model) {
		this.model = model;
		this.graph = model.getGraph();
	}

	@Override
	public void fillData(Map<String, Object> data) {
		data.put("solution.gapfound", lastGapNode);
	}

	@Override
	protected void main() throws IloException {
		if (super.getMIPRelativeGap() < lastGap) {
			lastGap = super.getMIPRelativeGap();
			lastGapNode = super.getNnodes();
		}
		
		int nodesSet = countNodesEqualOne(); 
		if (PruneEvaluator.shouldPrune(model, nodesSet)) {
			if (log) System.out.println("Pruning at " + nodesSet + " nodes set");
			prune(); return;
		}
		
		if (!enabled) return;

		// Clean up before starting
		branched = null;
		branchedNode = branchedColor = 0;
		
		// Calculate depth
		int depth = NodeData.getDepth(super.getNodeData()) + 1;
		
		// Calculate branching variable
		if (super.getNbranches() > 0 && getBranchType().equals(BranchType.BranchOnVariable)) {
			if (dynamicFractionalStrategy) {
				makeFractionalBranch();
			} else if (dynamicDSaturStrategy) {
				if (!continueConsecutiveColoring()) {
					makeDsaturBranch();
				}
			}
		}
		
		// If a variable was found, branch on it
		if (branched != null) { 
			NodeData data = new NodeData(depth, branchedNode, branchedColor, 0);
			if (branchSingle) {
				branchSingle(data);
			} else {
				branchUp(data);
				branchDown(data);
	        } return;
		}
		
		// Otherwise, manually branch on cplex recommended variable
		if (manual) {
			IloNumVar[][] varss = new IloNumVar[super.getNbranches()][];
			double[][] bounds = new double[super.getNbranches()][];
			BranchDirection[][] dirs = new BranchDirection[super.getNbranches()][];
			double[] branches = super.getBranches(varss, bounds, dirs);
			for (int i = 0; i < super.getNbranches(); i++) {
				if (log) System.out.println("Manually branching on " + Arrays.toString(varss[i]));
				super.makeBranch(varss[i], bounds[i], dirs[i], branches[i], new NodeData(depth));
			}
		}
		
	}

	/**
	 * Checks which was the last node-color combination used for branching and goes with the next color if previous branch was down
	 * @return true if a variable was chosen, false otherwise
	 * @throws IloException
	 */
	private boolean continueConsecutiveColoring() throws IloException {
		if (!consecColors) return false;
		
		NodeData nodeData = (NodeData) super.getNodeData();
		if (nodeData == null || !nodeData.isBranchDataSet() || nodeData.getBranchDirection() != -1) return false;
		
		int node = nodeData.getBranchedNode();
		int color = nodeData.getBranchedColor();
		
		for (int j = color+1; j < model.getColorCount(); j++) {
			IloIntVar nodevar = model.x(node, j);
			if (getFeasibility(nodevar).equals(IntegerFeasibilityStatus.Infeasible)
				&& getValue(nodevar) >= branchLB) {
				this.branchedColor = j;
				this.branchedNode = node;
				this.branched = nodevar;
				return true;
			}
		}
		
		return false;
	}

	private void branchSingle(NodeData data) throws IloException {
		if (log) System.out.println("Branching on single variable " + branched);
		super.makeBranch(branched, 1.0, BranchDirection.Up, getObjValue(), data.cloneWithDirection(1));
		super.makeBranch(branched, 0.0, BranchDirection.Down, getObjValue(), data.cloneWithDirection(-1));
	}

	private void branchUp(NodeData data) throws IloException {
		Node node = graph.getNode(branchedNode);
		Node[] partition = graph.getNodes(node.getPartition());
		Node[] adjs = graph.getNeighbours(node);
		
		// Initialize branch arrays to pass on to cplex
		TupleInt colorsToFix = colorCountToFix();
		int length = colorsToFix.getSecond() - colorsToFix.getFirst()
		    + model.getColorCount() * partition.length
		    + adjs.length;
		
		IloNumVar[] vars = new IloNumVar[length];
		double[] bounds = new double[length];
		BranchDirection[] dirs = new BranchDirection[length];
		int index = 0;
		
		// Set bounds for wj variables, all ceil(obj) are forced to one 
		for (int j = colorsToFix.getFirst(); j < colorsToFix.getSecond(); j++) {
			vars[index] = model.w(j);
			bounds[index] = 1.0;
			dirs[index] = BranchDirection.Up;
			index++;
		}
		
		// Set all nodes in the partition, except the chosen node with the chosen color, to zero
		for (Node n : partition) {
			for (int j = 0; j < model.getColorCount(); j++) {
				vars[index] = model.x(n.index, j);
				if (n.index == node.index && j == branchedColor) {
					bounds[index] = 1.0;
					dirs[index] = BranchDirection.Up;
				} else {
					bounds[index] = 0.0;
					dirs[index] = BranchDirection.Down;
				} index++;
			}
		}
		
		// For every neighbour, forbid usage of chosen color
		for (Node n : adjs) {
			vars[index] = model.x(n.index, branchedColor);
			bounds[index] = 0.0;
			dirs[index] = BranchDirection.Down;
			index++;
		}
		
		if (log) System.out.println("(" + NodeData.nodeDataToString(super.getNodeData()) + ") Branching up on variable " + branched + " for a total of " + length + " bounds set");
		super.makeBranch(vars, bounds, dirs, getObjValue(), data.cloneWithDirection(1));
	}
	
	private void branchDown(NodeData data) throws IloException {
		// Initialize branch arrays to pass on to cplex
		TupleInt colorsToFix = colorCountToFix();
		int length = colorsToFix.getSecond() - colorsToFix.getFirst() + 1;
		
		IloNumVar[] vars = new IloNumVar[length];
		double[] bounds = new double[length];
		BranchDirection[] dirs = new BranchDirection[length];
		
		// Set bounds for wj variables, all ceil(obj) are forced to one 
		int index = 0;
		for (int j = colorsToFix.getFirst(); j < colorsToFix.getSecond(); j++) {
			vars[index] = model.w(j);
			bounds[index] = 1.0;
			dirs[index] = BranchDirection.Up;
			index++;
		}
		
		// Branch down on chosen var
		vars[length-1] = branched;
		bounds[length-1] = 0.0;
		dirs[length-1] = BranchDirection.Down;
		
		if (log) System.out.println("(" + NodeData.nodeDataToString(super.getNodeData()) + ") Branching down on variable " + branched + " for a total of " + length + " bounds set");
		super.makeBranch(vars, bounds, dirs, getObjValue(), data.cloneWithDirection(-1));
	}
	
	private TupleInt colorCountToFix() throws IloException {
		if (!boundWs) return new TupleInt(0,0);
		
		int j = 0;
		while (j < model.getColorCount() && super.getLB(model.w(j)) == 1.0) j++;
		if (j == model.getColorCount()) return new TupleInt(0,0);
		
		double ws = objectiveStrategy.equals(Objective.Equal) 
			? super.getObjValue()
			: DoubleUtils.sum(super.getValues(model.getWs()));
		int newj = DoubleUtils.ceil(ws);
		
		TupleInt t = new TupleInt(j,newj);
		lastColorFixed = newj;
		return t;
	}
	
	private int assignColorsFromSolution(NodeSaturations saturs)
		throws IloException, AlgorithmException {
		
		switch(assignment) {
			case CheckAdjs: return assignColorsFromSolutionCheckingAdjs(saturs);
			case Fast: return assignColorsFromSolutionFast(saturs);
			case Safe: return assignColorsFromSolutionSafe(saturs);
			default: throw new AlgorithmException("Unknown dsatur assignment enum " + assignment);
		}
	}
	
	private int assignColorsFromSolutionCheckingAdjs(NodeSaturations saturs)
			throws IloException, AlgorithmException {
		int fixedCount = 0;
		for (Partition p : graph.getPartitions()) {
			part: for (Node n : graph.getNodes(p)) {
				int i = n.index;
				color: for (int j = 0; j < model.getColorCount(); j++) {
					IloIntVar x = model.x(i, j);
					double xval = super.getValue(x);
					if (xval > nodeLB) {
						for (Node adj : graph.getNeighbours(n)) {
							if (super.getValue(model.x(adj.index, j)) > xval) {
								continue color;
							}
						}
						
						fixedCount++;
						saturs.useColor(i, j);
						break part;
					}
				}				
			}
		}
		return fixedCount;
	}
	
	private int assignColorsFromSolutionFast(NodeSaturations saturs)
			throws IloException, AlgorithmException {
		int fixedCount = 0;
		for (Partition p : graph.getPartitions()) {
			part: for (Node n : graph.getNodes(p)) {
				int i = n.index;
				for (int j = 0; j < model.getColorCount(); j++) {
					IloIntVar x = model.x(i, j);
					if (super.getValue(x) > nodeLB) {
						fixedCount++;
						saturs.useColor(i, j);
						break part;
					}
				}				
			}
		}
		return fixedCount;
	}

	private int assignColorsFromSolutionSafe(IColorAssigner coloring) throws IloException, AlgorithmException {
		int fixedCount = 0;
		boolean[] colored = new boolean[graph.P()];
		
		// Iterate over nodes and colors checking for highest value among neighbours
		for (Node node : graph.getNodes()) {
			
			// Paint each partition only once regardless of the model
			if (colored[node.getPartition().index]) {
				continue;
			}
			
			for (int j = 0; j < model.getColorCount(); j++) {
				
				// Check if value is high enough to pass the lower bound
				double val = super.getValue(model.x(node.index, j));
				if (val < nodeLB) continue;
				boolean isCandidate = true;
				
				// Check if it has the highest value among neighbours
				for (Node adj : graph.getNeighbours(node)) {
					if (val <= super.getValue(model.x(adj.index, j))
					|| (val == super.getValue(model.x(adj.index, j)) && node.index > adj.index)) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Same for copartition
				for (Node adj : graph.getNodes(node.getPartition())) {
					if (adj.index != node.index && 
						((val <= super.getValue(model.x(adj.index, j)))
						|| (val == super.getValue(model.x(adj.index, j)) && node.index > adj.index))) {
						isCandidate = false;
						break;
					}
				}
				
				if (!isCandidate) {
					continue;
				}
				
				// Use that color for the node
				coloring.useColor(node.index, j);
				colored[node.getPartition().index] = true;
				fixedCount++;
				break;
			}
		}
		
		return fixedCount;
	}


	/**
	 * Picks a variable to branch using degree of saturation criteria
	 * @return variable to branch on
	 * @throws IloException
	 */
	private IloNumVar makeDsaturBranch() throws IloException {
		NodeSaturations saturs = new NodeSaturations(graph);
	
		try {
			int count = assignColorsFromSolution(saturs);
			if (log) System.out.println("Making dsatur branch having fixed " + count + " colors");
		} catch (Exception ex) {
			Logger.error("Error assigning coloring from solution in dsatur branching", ex);
			return null;
		}
		
		int bestNode = -1;
		int bestSatur = 0;
		int bestUncolored = 0;
		int bestPrio = 0;
		
		//Pick most saturated, highest prio node
		for (int i = 0; i < graph.N(); i++) {
			
			// Check if there is an integer infeasible value for some j
			IloIntVar var = null;
			for (int j = 0; j < model.getColorCount(); j++) {
				IloIntVar nodevar = model.x(i, j);
				if (getFeasibility(nodevar).equals(IntegerFeasibilityStatus.Infeasible)
					&& getValue(nodevar) >= branchLB) {
					var = nodevar;
					break;
				}
			}
			
			// If there isnt, this node is not considered for branching
			if (var == null) {
				continue;
			}
			
			int satur = saturs.getSaturation(i);
			int uncolored = saturs.getUncoloredNeighbours(i);
			int prio = super.getPriority(var);

			if ((bestSatur < satur)  
				|| (bestSatur == satur && bestUncolored < uncolored)
				|| (bestSatur == satur && bestUncolored == uncolored && bestPrio < prio)) {
				bestNode = i;
				bestSatur = satur;
				bestPrio = prio;
				bestUncolored = uncolored;
			}
		}
		
		// If no node was found, dont branch on dsatur criteria
		if (bestNode == -1) {
			return null;
		}
		
		// Branch on highest value color or first color, depending if consecutive color branching is enabled
		if (consecColors) {
			return pickFirstColor(bestNode);
		} else {
			return pickHighestColorValue(bestNode);
		}
	}
	
	private IloNumVar pickFirstColor(int bestNode) throws IloException {
		for (int j = 0; j < model.getColorCount(); j++) {
			IloIntVar nodevar = model.x(bestNode, j);
			if (getFeasibility(nodevar).equals(IntegerFeasibilityStatus.Infeasible)
				&& getValue(nodevar) >= branchLB) {
				
				this.branched = nodevar;
				this.branchedColor = j;
				this.branchedNode = bestNode;
				return branched;
			}
		}
		
		return null;
	}


	private IloNumVar pickHighestColorValue(int bestNode) throws IloException {
		double bestVal = 0.0;
		int bestColor = 0;
		IloNumVar branched = null;
		
		for (int j = 0; j < model.getColorCount(); j++) {
			IloNumVar var = model.x(bestNode, j);
			double val = getValue(var);
			if (val > bestVal) {
				bestVal = val;
				branched = var;
				bestColor = j;
			}
		}
		
		this.branched = branched;
		this.branchedColor = bestColor;
		this.branchedNode = bestNode;
		return branched;
	}

	private IloNumVar makeFractionalBranch() throws IloException {
		IloNumVar[] vars = model.getAllXs();
		double[] values = getValues(vars);
		IntegerFeasibilityStatus[] feasibilities = getFeasibilities(vars);
		
		IloNumVar branched = null;
		int bestPrio = 0;
		int bestColor = 0;
		int bestNode = 0;
		double mostFrac = 0.0;
		
		int nodeCount = model.getNodeCount();
		int colorCount = model.getColorCount();
		
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < colorCount; j++) {
				int k = i * colorCount + j;
				if (feasibilities[k].equals(IntegerFeasibilityStatus.Infeasible)) {
					double frac = DoubleUtils.fractionality(values[k]);
					int prio = getPriority(vars[k]);
					
					// Keep most or less frac based on cfg
					if ((BranchCallback.mostFrac && branchMostFrac(branched, bestPrio, mostFrac, frac, prio)) ||
						(!BranchCallback.mostFrac && branchLessFrac(branched, bestPrio, mostFrac, frac, prio))) {
						bestColor = j;
						bestNode = i;
						branched = vars[k];
						bestPrio = prio;
						mostFrac = frac;
					}
				}
			}
		}
		
		this.branchedColor = bestColor;
		this.branchedNode = bestNode;
		this.branched = branched;
		return branched;
	}

	private boolean branchMostFrac(IloNumVar branched, int bestPrio, double mostFrac, double frac, int prio) {
		return (branched == null 
			|| frac > mostFrac + fracTol 
			|| (frac > mostFrac - fracTol && prio > bestPrio)
			|| (frac > mostFrac && bestPrio == 0));
	}
	
	private boolean branchLessFrac(IloNumVar branched, int bestPrio, double mostFrac, double frac, int prio) {
		return (branched == null 
			|| frac < mostFrac - fracTol 
			|| (frac < mostFrac + fracTol && prio > bestPrio)
			|| (frac < mostFrac && bestPrio == 0));
	}
	
	private int countNodesEqualOne() throws IloException {
		IntegerFeasibilityStatus[] feasibilities = super.getFeasibilities(model.getAllXs());
		double[] lbs = super.getLBs(model.getAllXs());
		return ModelUtils.countNodesFixedToOne(feasibilities, lbs);

	}
	
	
	
}
