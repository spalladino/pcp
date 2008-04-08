package pcp.solver.branching;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.model.Model;
import props.Settings;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;


public class BranchingPrioritizer {
	
	final static boolean enabled = Settings.get().getBoolean("branch.prios.enabled");
	final static int partitionSizeFactor = Settings.get().getInteger("branch.prios.psize");
	final static int partitionsAdjFactor = Settings.get().getInteger("branch.prios.psadjacent");
	final static int colorIndexFactor = Settings.get().getInteger("branch.prios.colorindex");
	final static int reverseColorIndexFactor = Settings.get().getInteger("branch.prios.reversecolorindex");
	
	IloCplex cplex;
	Model model;
	
	public BranchingPrioritizer(IloCplex cplex, Model model) {
		this.cplex = cplex;
		this.model = model;
	}
	
	public BranchingPrioritizer setPriorities() throws IloException {
		if (!enabled) return this;
		PartitionedGraph graph = model.getGraph();
		
		for (int i = 0; i < model.getNodeCount(); i++) {
			
			Node node = graph.getNode(i);
			int nodeprio = graph.getNeighbourPartitions(node).length * partitionsAdjFactor +
					node.getPartition().getNodes().length * partitionSizeFactor;
			
			for (int j = 0; j < model.getColorCount(); j++) {
				IloIntVar var = model.x(i, j);
				int prio = nodeprio 
					+ (model.getColorCount() - j) * colorIndexFactor
					+ j * reverseColorIndexFactor;
				cplex.setPriority(var, prio);
			}
		}
		
		return this;
	}
	
	
}
