package pcp.model;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloObjective;

import java.util.List;
import java.util.Map;

import pcp.algorithms.clique.CliqueCover;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.InducedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.utils.GraphUtils;
import props.Settings;
import exceptions.AlgorithmException;

public class ModelBuilder {
	
	static final boolean useCliqueCover = Settings.get().getBoolean("model.adjacentsNeighbourhood.useCliqueCover");
	
	IloIntVar[] allxs;
	IloIntVar[][] xs;
	IloIntVar[] ws;
	IloObjective objective;
	
	protected PartitionedGraph graph;
	protected IloMPModeler modeler;
	
	protected int colors;
	
	public ModelBuilder(PartitionedGraph graph, IloMPModeler modeler) {
		this.graph = graph;
		this.modeler = modeler;
	}

	public Model buildModel(BuilderStrategy strategy, ColoringAlgorithm coloring) throws IloException, AlgorithmException {
		this.xs = null;
		this.ws = null;
		this.objective = null;
		
		Model model = new Model(this.graph);
		model.strategy = strategy;
		this.colors = model.colors = coloring.getChi();
		
		// Initialize variables and objective function
		initializeVariables();
		createObjective();
		
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
		
		// Breaking symmetry constraints
		switch(strategy.getBreakSymmetry()) {
			case OnlyUseColorIfNodesPainted:
				constrainUseLowerLabelFirst();
				constrainUseColorIfNodesPainted();
				break;
			case UseLowerLabelFirstStrengthened:
				constrainUseLowerLabelFirst();
				constrainLowerLabelStrengthened();
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
		fillModel(model);
		
		return model;
	}

	private void fillModel(Model model) {
		model.xs = xs;
		model.allxs = allxs;
		model.ws = ws;
		model.objective = objective;
	}
	
	/**
	 * Initializes all variables using the current color count.
	 * @throws IloException
	 */
	protected void initializeVariables() throws IloException {
		// Initialize objects
		Integer nodes = graph.getNodes().length;
		this.allxs = new IloIntVar[nodes*colors];
		this.xs = new IloIntVar[nodes][colors];
		this.ws = new IloIntVar[colors];
		
		// Create xs variables
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < colors; j++) {
				this.xs[i][j] = modeler.boolVar(String.format("x[%1$d,%2$d]", i, j));
				this.allxs[i * colors + j] = this.xs[i][j]; 
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
	protected void createObjective() throws IloException {
		IloLinearIntExpr obj = modeler.linearIntExpr();
		for (int j = 0; j < colors; j++) {
			obj.addTerm(ws[j], 1);
		}
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
	 * Creates symmetry break constraints sum_j w_j \geq sum_j j x_ij 
	 * @throws IloException 
	 */
	protected void constrainLowerLabelStrengthened() throws IloException {
		for (Node n : graph.getNodes()) {
			IloLinearIntExpr expr = modeler.linearIntExpr();
			String name = String.format("BSSTR[%1$d]", n.index());
			for (int j = 0; j < colors; j++) {
				expr.addTerm(ws[j], 1);
				expr.addTerm(xs[n.index()][j], -j);
			}
			
			modeler.addGe(expr, 0, name);
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
				expr.addTerm(xs[n.index()][j], 1);
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
				String name = String.format("X[%1$d,%2$d]", n.index(), j);
				expr.addTerm(xs[n.index()][j], 1);
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
					String name = String.format("ADJP[%1$d,%2$d]", n.index(), j);
					// x_i0j0
					expr.addTerm(xs[n.index()][j], 1);
					// sum i \in p x_ij0
					for (Node adj : adjs) {
						expr.addTerm(xs[adj.index()][j], 1);
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
				String name = String.format("ADJN[%1$d,%2$d]", n.index(), j);
				// r * x_i0j0
				expr.addTerm(xs[n.index()][j], r);
				// sum i \in N(i0) x_ij0
				for (Node adj : graph.getNeighbours(n)) {
					expr.addTerm(xs[adj.index()][j], 1);
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
				String name = String.format("E[%1$d,%2$d]", e.getNode1().index(), e.getNode2().index());
				expr.addTerm(xs[e.getNode1().index()][j], 1);
				expr.addTerm(xs[e.getNode2().index()][j], 1);
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
				String name = String.format("E[%1$d,%2$d]", e.getNode1().index(), e.getNode2().index());
				expr.addTerm(xs[e.getNode1().index()][j], 1);
				expr.addTerm(xs[e.getNode2().index()][j], 1);
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
			String name = String.format("P[%1$d]", p.index());
			for (int j = 0; j < colors; j++) {
				for (Node n : graph.getNodes(p)) {
					expr.addTerm(xs[n.index()][j], 1);
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
			String name = String.format("P[%1$d]", p.index());
			for (int j = 0; j < colors; j++) {
				for (Node n : graph.getNodes(p)) {
					expr.addTerm(xs[n.index()][j], 1);
				}
			}
			modeler.addGe(expr, 1, name);
		}
	}
	
}
