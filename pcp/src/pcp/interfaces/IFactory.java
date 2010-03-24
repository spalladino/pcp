package pcp.interfaces;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.solver.Solver;


public interface IFactory {

	public ColoringAlgorithm coloring(pcp.model.strategy.Coloring coloring, IPartitionedGraph graph);

	public PartitionedGraphBuilder getGraphBuilder(String filename) throws Exception;

	public Solver createSolver(pcp.solver.Kind kind) throws Exception;
}
