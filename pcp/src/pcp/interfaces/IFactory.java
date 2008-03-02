package pcp.interfaces;

import pcp.algorithms.coloring.Coloring;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.solver.Solver;


public interface IFactory {

	public Coloring coloring(pcp.model.strategy.Coloring coloring, PartitionedGraph graph);

	public PartitionedGraphBuilder getGraphBuilder(String filename) throws Exception;

	public Solver createSolver(pcp.solver.Kind kind) throws Exception;
}
