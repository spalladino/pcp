package pcp.model;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;

import java.util.List;
import java.util.Map;

import pcp.algorithms.clique.CliqueCover;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.common.sorting.NodeDegreeCompleteComparator;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.InducedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.SortedPartitionedGraph;
import pcp.model.strategy.Objective;
import pcp.model.strategy.Symmetry;
import pcp.utils.GraphUtils;
import props.Settings;
import exceptions.AlgorithmException;

public class ModelBuilder {
	
	static final boolean useCliqueCover = Settings.get().getBoolean("model.adjacentsNeighbourhood.useCliqueCover");
	static final boolean boundVariablesOnDegree = Settings.get().getBoolean("model.variables.boundOnDegree");
	static final boolean boundVariablesOnPartitionIndex = Settings.get().getBoolean("model.variables.boundOnPartitionIndex");
	static final boolean fixClique = Settings.get().getBoolean("model.variables.fixClique");
	static final boolean useSOS = Settings.get().getBoolean("model.useSOS");
	
	IloIntVar[] allxs;
	IloIntVar[][] xs;
	IloIntVar[] ws;
	IloObjective objective;
	IloIntExpr colorsum;
	
	protected PartitionedGraph graph;
	protected IloMPModeler modeler;
	
	protected int colors;
	protected int initialCliqueSize;
	
	public ModelBuilder(PartitionedGraph graph, IloMPModeler modeler) {
		this.graph = graph;
		this.modeler = modeler;
	}

	public Model buildModel(BuilderStrategy strategy, ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		return buildModel(strategy, coloring, null);
	}
	
	public Model buildModel(BuilderStrategy strategy, ColoringAlgorithm coloring, List<pcp.entities.simple.Node> maxgpclique) throws IloException, AlgorithmException {
		this.xs = null;
		this.ws = null;
		this.objective = null;
		
		Model model = new Model(this.graph);
		model.strategy = strategy;
		
		this.colors = model.colors = coloring.getChi();
		
		// Initialize variables and objective function
		initializeVariables(strategy);
		boundClique(coloring, maxgpclique);
		
		// Objective function
		createObjective(strategy.getObjective());
		
		// Constraints based on painting
		switch (strategy.getPartitionConstraints()) {
			case PaintExactlyOne:
				constrainEachPartitionOneColor();
				break;
			case PaintAtLeastOne:
				constrainEachPartitionAtLeastOneColor();
				break;
			default:
				throw new UnsupportedOperationException("Unhandled model builder strategy: " + strategy.getPartitionConstraints());
		}
		if (useSOS) {
			createSOS();
		}
		
		// Adjacency constraints
		switch(strategy.getAdjacencyConstraints()) {
			case AdjacentsLeqColor:
				constrainAdjacentLessThanColor();
				break;
			case AdjacentsLeqOne:
				constrainNodeLessThanColor();
				constrainAdjacentNotEquals();
				break;
			case AdjacentsNeighbourhood:
				constrainAdjacencyNeighbourhood();
				break;
			case AdjacentsPartitionLeqColor:
				constrainAdjacencyPerPartitionLeqColor();
				break;
			default:
				throw new UnsupportedOperationException("Unhandled model builder strategy: " + strategy.getAdjacencyConstraints());
		}
		
		// Apply bounds on wjs and sums
		switch(strategy.getColorBound()) {
			case UpperNodesSumLowerSumPartition:
				constrainUseColorIfNodesPainted();
				constrainColorSumStrengthenedPartition();
				break;
			case UpperNodesSumLowerSum:
				constrainUseColorIfNodesPainted();
				constrainColorSumStrengthened();
				break;
			case UpperNodesSum:
				constrainUseColorIfNodesPainted();
				break;
			case None:
				break;
			default:
				throw new UnsupportedOperationException("Unhandled model builder strategy: " + strategy.getColorBound());
		}
		
		// Breaking symmetry constraints
		switch(strategy.getBreakSymmetry()) {
			case MinimumNodeLabel:
				constrainSymmetryMinimumNodeLabel();
				constrainUseLowerLabelFirst();
				break;
			case VerticesNumber:
				constrainSymmetryVerticesNumber();
				constrainUseLowerLabelFirst();
				break;
			case UseLowerLabelFirst:
				constrainUseLowerLabelFirst();
				break;
			case None:
				break;
			default:
				throw new UnsupportedOperationException("Unhandled model builder strategy: " + strategy.getBreakSymmetry());
		}
		
		// Copy all vars and objective to model
		return fillModel(model);
	}

	/**
	 * Fixes variables for nodes within the max clique.
	 * Every variable with color different than the assigned is set to zero.
	 * @param maxgpclique max clique in g prime graph
	 * @throws IloException 
	 * @throws AlgorithmException 
	 */
	private void boundClique(ColoringAlgorithm coloring, List<pcp.entities.simple.Node> maxgpclique) throws IloException, AlgorithmException {
		if (!fixClique || maxgpclique == null) return;
		
		this.initialCliqueSize = maxgpclique.size();
		for (pcp.entities.simple.Node snode : maxgpclique) {
			int color = coloring.getPartitionColor(snode.index());
			
			// Set all colors for the partition in the clique to zero, except assigned one
			Node[] nodes = graph.getNodes(snode);
			for (Node node : nodes) {
				for (int j = 0; j < colors; j++) {
					IloIntVar var = this.xs[node.index][j];
					if (j != color) var.setUB(0.0);
				}
			}
			// If there is a single node, fix it
			if (nodes.length == 1) {
				IloIntVar var = this.xs[nodes[0].index][color];
				var.setLB(1.0);
			}
			
			// Fix color variable as well
			IloIntVar var = this.ws[color];
			var.setLB(1.0);
		}
	}

	private Model fillModel(Model model) throws IloException {
		model.xs = xs;
		model.allxs = allxs;
		model.ws = ws;
		model.objective = objective;
		model.colorsum = colorsum;
		model.model = modeler.getModel();
		return model;
	}
	
	/**
	 * Initializes all variables using the current color count.
	 * @throws IloException
	 */
	protected void initializeVariables(BuilderStrategy strategy) throws IloException {
		// Initialize objects
		Integer nodes = graph.getNodes().length;
		this.allxs = new IloIntVar[nodes*colors];
		this.xs = new IloIntVar[nodes][colors];
		this.ws = new IloIntVar[colors];
		
		// Create xs variables
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < colors; j++) {
				IloIntVar var = modeler.boolVar(String.format("x[%1$d,%2$d]", i, j));
				Node node = graph.getNode(i);
				if (boundVariablesOnDegree && j > graph.getNeighbourPartitions(node).length) {
					var.setUB(0.0);
				} else if (boundVariablesOnPartitionIndex && strategy.breakSymmetry.equals(Symmetry.MinimumNodeLabel) && j > graph.getPartition(node).index) {
					var.setUB(0.0);
				}
				
				this.xs[i][j] = var;
				this.allxs[i * colors + j] = var; 
			}
		}
		
		// Create ws variables
		for (int j = 0; j < colors; j++) {
			this.ws[j] = modeler.boolVar(String.format("w[%1$d]", j));
		}
	}

	/**
	 * Creates the default objective function
	 * @throws IloException
	 */
	protected void createObjective(Objective strategy) throws IloException {
		IloLinearIntExpr obj = modeler.linearIntExpr();
		IloLinearIntExpr colorsum = modeler.linearIntExpr();
		
		for (int j = 0; j < colors; j++) {
			int multiplier = 1;
			switch (strategy) {
				case LinearReverse: multiplier = colors - j; break;
				case Linear: multiplier = j + 1; break;
				case Geometric: multiplier = (j + 1) * colors; break;
				default: multiplier = 1; break;
			}
			
			obj.addTerm(ws[j], multiplier);
			colorsum.addTerm(ws[j], 1);
		} 
		
		this.colorsum = colorsum;
		this.objective = modeler.addMinimize(obj);
	}

	/**
	 * Creates symmetry break constraints wj geq wj+1
	 * @throws IloException 
	 */
	protected void constrainUseLowerLabelFirst() throws IloException {
		for (int j = 0; j < colors-1; j++) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BS[%1$d]", j);
			expr.addTerm(ws[j], 1);
			expr.addTerm(ws[j+1], -1);
			modeler.addGe(expr, 0, name);
		}
	}

	/**
	 * Creates symmetry break constraints sum_i x_ij \geq sum_i x_i(j+1)
	 * @throws IloException 
	 */
	protected void constrainSymmetryVerticesNumber() throws IloException {
		for (int j = 0; j < colors-1; j++) {
			// We ensure there is no relation between labels in the initial clique and outside
			if (fixClique && initialCliqueSize > 0 && j <= initialCliqueSize) continue;
			
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BSVNUM[%1$d]", j);
			for (Node n : graph.getNodes()) {
				expr.addTerm(xs[n.index][j], 1);
				expr.addTerm(xs[n.index][j+1], -1);
			}

			modeler.addGe(expr, 0, name);
		}
	}

	/**
	 * Creates fractional break constraints x_ij \leq sum_k x_k(j-1) forall i\x1, 0<j<i
	 * @throws IloException 
	 */
	protected void constrainSymmetryMinimumNodeLabel() throws IloException {
		for (int k = 0; k < graph.P(); k++) {
			Partition partition = graph.getPartition(k);
			
			// Set xij = 0 for every color greater than partition index
			for (int j = k+1; j < colors; j++) {
				for (Node node : graph.getNodes(partition)) {
					xs[node.index][j].setUB(0.0);
				}
			}
			
			// Set minimum label constraints
			if (k == 0) continue;
			for (int j = 1; j <= k && j < colors; j++) {
				for (Node node : graph.getNodes(partition)) {
					// Fixed k, i and j
					int i = node.index;
					IloLinearIntExpr expr = modeler.linearIntExpr();
					String name = String.format("BSMINLBL[%1$d,%2$d]", i, j);
					expr.addTerm(xs[i][j], 1);
					for (int l = j-1; l < k; l++) {
						for (Node u : graph.getNodes(graph.getPartition(l))) {
							expr.addTerm(xs[u.index][j-1], -1);
						}
					}			
					modeler.addLe(expr, 0, name);
				}
			}
		}
	}

	/**
	 * Creates fractional break constraints sum_j w_j \geq sum_j x_ij 
	 * @throws IloException 
	 */
	protected void constrainColorSumStrengthened() throws IloException {
		for (Node n : graph.getNodes()) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BSSTR[%1$d]", n.index);
			for (int j = 0; j < colors; j++) {
				expr.addTerm(ws[j], 1);
				expr.addTerm(xs[n.index][j], -j);
			}
			
			modeler.addGe(expr, 0, name);
		}
	}

	/**
	 * Creates fractional break constraints sum_j w_j \geq sum_i \in p sum_j j x_ij 
	 * @throws IloException 
	 */
	protected void constrainColorSumStrengthenedPartition() throws IloException {
		for (Partition p : graph.getPartitions()) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BSSTRP[%1$d]", p.index);
			for (Node n : graph.getNodes()) {
				for (int j = 0; j < colors; j++) {
					expr.addTerm(ws[j], 1);
					expr.addTerm(xs[n.index][j], -j);
				}
			} modeler.addGe(expr, 0, name);
		}
	}

	/**
	 * Creates symmetry break constraints wj leq sum xij
	 * @throws IloException 
	 */
	protected void constrainUseColorIfNodesPainted() throws IloException {
		for (int j = 0; j < colors; j++) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("FC[%1$d]", j);
			for (Node n : graph.getNodes()) {
				expr.addTerm(xs[n.index][j], 1);
			}
			
			expr.addTerm(ws[j], -1);
			modeler.addGe(expr, 0, name);
		}
	}

	/**
	 * Create restrictions Xij leq Wj
	 * @throws IloException
	 */
	protected void constrainNodeLessThanColor() throws IloException {
		for (Node n : graph.getNodes()) {
			for (int j = 0; j < colors; j++) {
				IloLinearIntExpr expr = modeler.linearIntExpr();
				String name = String.format("X[%1$d,%2$d]", n.index, j);
				expr.addTerm(xs[n.index][j], 1);
				expr.addTerm(ws[j], -1);
				modeler.addLe(expr, 0, name);
			}
		}
	}

	/**
	 * Creates constraints sum_{i \in p & N(i0)} x_ij0 + x_i0j0 \leq w_j0 \forall j0, p, i0
	 * @throws IloException 
	 */
	protected void constrainAdjacencyPerPartitionLeqColor() throws IloException {
		// Iterate for every i0
		for (Node n : graph.getNodes()) {
			Map<Partition, List<Node>> groupByPartition = GraphUtils.groupByPartition(graph.getNeighbours(n));
			// Iterate on neighbouring partitions
			for (List<Node> adjs : groupByPartition.values()) {
				// Iterate on colors
				for (int j = 0; j < colors; j++) {
					IloLinearIntExpr expr = modeler.linearIntExpr();
					String name = String.format("ADJP[%1$d,%2$d]", n.index, j);
					// x_i0j0
					expr.addTerm(xs[n.index][j], 1);
					// sum i \in p x_ij0
					for (Node adj : adjs) {
						expr.addTerm(xs[adj.index][j], 1);
					}
					// w_j0
					expr.addTerm(ws[j], -1);
					modeler.addLe(expr, 0, name);
				}
			}
		}
	}

	/**
	 * Creates constraints sum_{i \in N(i0)} x_ij0 + r * x_i0j0 \leq r* w_j0 \forall j0, i0
	 * with r = number of partitions in i0's neighbourhood
	 */
	protected void constrainAdjacencyNeighbourhood() throws IloException {
		for (Node n : graph.getNodes()) {
			final int r = useCliqueCover 
				? new CliqueCover(new InducedGraph(graph, graph.getNeighbours(n)).getGPrime()).count()
				: GraphUtils.groupByPartition(graph.getNeighbours(n)).size();
			
			for (int j = 0; j < colors; j++) {
				IloLinearIntExpr expr = modeler.linearIntExpr();
				String name = String.format("ADJN[%1$d,%2$d]", n.index, j);
				// r * x_i0j0
				expr.addTerm(xs[n.index][j], r);
				// sum i \in N(i0) x_ij0
				for (Node adj : graph.getNeighbours(n)) {
					expr.addTerm(xs[adj.index][j], 1);
				}
				// r * w_j0
				expr.addTerm(ws[j], -r);
				modeler.addLe(expr, 0, name);
			}
		}
	}

	/**
	 * Create restrictions Xij + Xkj leq 1
	 * @throws IloException
	 */
	protected void constrainAdjacentNotEquals() throws IloException {
		for (Edge e : graph.getEdges()) {
			for (int j = 0; j < colors; j++) {
				IloLinearIntExpr expr = modeler.linearIntExpr();
				String name = String.format("E[%1$d,%2$d]", e.node1.index, e.node2.index);
				expr.addTerm(xs[e.node1.index][j], 1);
				expr.addTerm(xs[e.node2.index][j], 1);
				modeler.addLe(expr, 1, name);
			}	
		}
	}
	
	/**
	 * Create restrictions Xij + Xkj leq Wj
	 * @throws IloException
	 */
	protected void constrainAdjacentLessThanColor() throws IloException {
		for (Edge e : graph.getEdges()) {
			for (int j = 0; j < colors; j++) {
				IloLinearIntExpr expr = modeler.linearIntExpr();
				String name = String.format("E[%1$d,%2$d]", e.node1.index, e.node2.index);
				expr.addTerm(xs[e.node1.index][j], 1);
				expr.addTerm(xs[e.node2.index][j], 1);
				expr.addTerm(ws[j], -1);
				modeler.addLe(expr, 0, name);
			}	
		}
	}
	
	/**
	 * Create restrictions SUMj SUMq Xqj = 1 
	 * @throws IloException
	 */
	protected void constrainEachPartitionOneColor() throws IloException {
		for (Partition p : graph.getPartitions()) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("P[%1$d]", p.index);
			for (int j = 0; j < colors; j++) {
				for (Node n : graph.getNodes(p)) {
					expr.addTerm(xs[n.index][j], 1);
				}
			}
			modeler.addEq(expr, 1, name);
		}
	}
	
	/**
	 * Create restrictions SUMj SUMq Xqj geq 1 
	 * @throws IloException
	 */
	protected void constrainEachPartitionAtLeastOneColor() throws IloException {
		for (Partition p : graph.getPartitions()) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("P[%1$d]", p.index);
			for (int j = 0; j < colors; j++) {
				for (Node n : graph.getNodes(p)) {
					expr.addTerm(xs[n.index][j], 1);
				}
			}
			modeler.addGe(expr, 1, name);
		}
	}

	/**
	 * Creates a special order set for each partition
	 * @throws IloException 
	 */
	protected void createSOS() throws IloException {
		SortedPartitionedGraph sg = new SortedPartitionedGraph(graph, new NodeDegreeCompleteComparator(true), null, null);
		
		for (Partition p : sg.getPartitions()) {
			String name = String.format("SOS[%1$d]", p.index);
			int index = 0;
			int size = graph.getNodes(p).length * colors;
			IloNumVar[] vars = new IloNumVar[size];
			double[] vals = new double[size];
			
			for (Node n : sg.getNodes(p)) {
				for (int j = 0; j < colors; j++) {
					vars[index] = xs[n.index][j];
					vals[index] = index;
					index++;
				}
			}

			modeler.addSOS1(vars, vals, name);
		}
		
	}
	
}
