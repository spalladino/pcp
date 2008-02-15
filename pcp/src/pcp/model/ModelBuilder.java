package pcp.model;

import java.util.HashSet;
import java.util.Set;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloMPModeler;
import ilog.concert.IloObjective;
import pcp.Factory;
import pcp.algorithms.AlgorithmException;
import pcp.entities.Edge;
import pcp.entities.Node;
import pcp.entities.Partition;
import pcp.entities.PartitionedGraph;
import pcp.model.strategy.Adjacency;
import pcp.model.strategy.Symmetry;

public class ModelBuilder {
	
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

	public Model buildModel(BuilderStrategy strategy) throws IloException, AlgorithmException {
		this.xs = null;
		this.ws = null;
		this.objective = null;
		
		Model model = new Model(this.graph);
		this.colors = model.colors = Factory.get().coloring(strategy.getColoring(), graph).getChi();
		
		// Initialize variables and objective function
		initializeVariables();
		createObjective();
		
		// Constraints based on strategy
		if (strategy.getPartitionConstraints() == pcp.model.strategy.Partition.PaintExactlyOne) {
			constrainEachPartitionOneColor();
		} else {
			constrainEachPartitionAtLeastOneColor();
		}
		
		if (strategy.getAdjacencyConstraints() == Adjacency.AdjacentsLeqColor) {
			constrainAdjacentLessThanColor();
		} else if (strategy.getAdjacencyConstraints() == Adjacency.AdjacentsLeqOne) {
			constrainNodeLessThanColor();
			constrainAdjacentNotEquals();
		} else if (strategy.getAdjacencyConstraints() == Adjacency.AdjacentsNeighbourhood) {
			constrainAdjacencyNeighbourhood();
		} else {
			throw new UnsupportedOperationException("Unhandled adjacency strategy");
		}
		
		
		if (strategy.getBreakSymmetry() == Symmetry.OnlyUseColorIfNodesPainted) {
			constrainUseLowerLabelFirst();
			constrainUseColorIfNodesPainted();
		} else if (strategy.getBreakSymmetry() == Symmetry.UseLowerLabelFirst) {
			constrainUseLowerLabelFirst();
		}
		
		
		
		model.xs = xs;
		model.ws = ws;
		model.objective = objective;
		
		return model;
	}
	
	/**
	 * Initializes all variables using the current color count.
	 * @throws IloException
	 */
	protected void initializeVariables() throws IloException {
		// Initialize objects
		Integer nodes = graph.getNodes().length;
		this.xs = new IloIntVar[nodes][colors];
		this.ws = new IloIntVar[colors];
		
		// Create xs variables
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < colors; j++) {
				this.xs[i][j] = modeler.boolVar(String.format("x[%1$d,%2$d]", i, j));
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
			String name = String.format("BS%1$d", j);
			expr.addTerm(ws[j], 1);
			expr.addTerm(ws[j+1], -1);
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

	protected void constrainAdjacencyNeighbourhood() throws IloException {
		for (Node n : graph.getNodes()) {
			Set<Partition> neighbourPartitions = new HashSet<Partition>();
			for (Node adj : n.getNeighbours()) {
				neighbourPartitions.add(adj.getPartition());
			}
			
			final int r = neighbourPartitions.size();
			for (int j = 0; j < colors; j++) {
				IloLinearIntExpr expr = modeler.linearIntExpr();
				String name = String.format("ADJN[%1$d,%2$d]", n.index(), j);
				// r * x_i0j0
				expr.addTerm(xs[n.index()][j], r);
				// sum i \in N(i0) x_ij0
				for (Node adj : n.getNeighbours()) {
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
				for (Node n : p.getNodes()) {
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
				for (Node n : p.getNodes()) {
					expr.addTerm(xs[n.index()][j], 1);
				}
			}
			modeler.addGe(expr, 1, name);
		}
	}
	
}
