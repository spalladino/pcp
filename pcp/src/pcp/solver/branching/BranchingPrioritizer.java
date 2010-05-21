package pcp.solver.branching;

import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.model.Model;
import pcp.utils.IntUtils;
import props.Settings;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;


public class BranchingPrioritizer {
	
	final static boolean enabled = Settings.get().getBoolean("branch.prios.enabled");
	
	final static int inputPartitionSizeFactor = Settings.get().getInteger("branch.prios.psize");
	final static int inputPartitionsAdjFactor = Settings.get().getInteger("branch.prios.psadjacent");
	final static int inputColorIndexFactor = Settings.get().getInteger("branch.prios.colorindex");
	
	final static int partitionSizeFactor = IntUtils.positiveOrZero(inputPartitionSizeFactor);
	final static int partitionsAdjFactor = IntUtils.positiveOrZero(inputPartitionsAdjFactor);
	final static int colorIndexFactor = IntUtils.positiveOrZero(inputColorIndexFactor);
	
	final static int reversePartitionSizeFactor = -IntUtils.negativeOrZero(inputPartitionSizeFactor);
	final static int reversePartitionsAdjFactor = -IntUtils.negativeOrZero(inputPartitionsAdjFactor);
	final static int reverseColorIndexFactor = -IntUtils.negativeOrZero(inputColorIndexFactor);
	
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
			int nodeprio = 
					graph.getNeighbourPartitions(node).length * partitionsAdjFactor +
					graph.getNodes(node.getPartition()).length * partitionSizeFactor +
					(graph.P() - graph.getNeighbourPartitions(node).length) * reversePartitionsAdjFactor +
					(graph.N() - graph.getNodes(node.getPartition()).length) * reversePartitionSizeFactor;
			
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
