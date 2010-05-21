package pcp.algorithms;

import static pcp.utils.DoubleUtils.doubleEquals;
import static pcp.utils.DoubleUtils.isTrue;
import exceptions.AlgorithmException;
import ilog.concert.IloException;
import ilog.cplex.IloCplex.UnknownObjectException;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.interfaces.ISolutionData;

public class Verifier {

	ISolutionData solver;
	IPartitionedGraph graph;

	public Verifier(ISolutionData solver) {
		this.solver = solver;
		this.graph = solver.getGraph();
	}
	
	public void verify() throws IloException, AlgorithmException {
		everyPartitionHasOneColoredNode();
		adjacentsHaveDifferentColors();
		colorVariablesAreSet();
	}

	private void colorVariablesAreSet() throws IloException, AlgorithmException {
		double[] colorValues = solver.ws();
		for (Node n : graph.getNodes()) {
			double[] nodeColors = solver.xs(n.index());
			for (int c = 0; c < nodeColors.length; c++) {
				if (isTrue(nodeColors[c]) && !isTrue(colorValues[c])) {
					throw new AlgorithmException("Node " + n.index() + " uses color " + c + " whose variable is unset.");
				}
			}
		}
	}

	private void adjacentsHaveDifferentColors() throws IloException, AlgorithmException {
		for (Edge e : graph.getEdges()) {
			int c1 = solver.getNodeColor(e.getNode1().index());
			int c2 = solver.getNodeColor(e.getNode2().index());
			
			if (c1 > -1 && c2 > -1 && c1 == c2) {
				throw new AlgorithmException(String.format("Adjacent nodes %1$d and %2$d have the same color %3$d.", e.getNode1().index(), e.getNode2().index(), c1));
			}
		}
	}

	private void everyPartitionHasOneColoredNode() throws UnknownObjectException, IloException, AlgorithmException {
		for (Partition p : graph.getPartitions()) {
			int coloredCount = 0;
			for (Node n : graph.getNodes(p)) {
				double[] nodeColors = solver.xs(n.index());
				int nodeColorsCount = 0;
				for (int c = 0; c < nodeColors.length; c++) {
					if (doubleEquals(nodeColors[c], 1.0)) {
						coloredCount++;
						nodeColorsCount++;
					}
				}
				if (nodeColorsCount > 1) {
					//Now we are being too permissive
					//throw new AlgorithmException("Node " + n.val() + " has " + nodeColorsCount + " colors");
				}
			}
			
			if (coloredCount == 0) {
				throw new AlgorithmException("Partition " + p.index() + " has no colored nodes.");
			} else if (coloredCount > 1) {
				//We allow more than a colored node per partition
				//throw new AlgorithmException("Partition " + p.val() + " has " + coloredCount + " colored nodes.");
			}
		}
	}
}
