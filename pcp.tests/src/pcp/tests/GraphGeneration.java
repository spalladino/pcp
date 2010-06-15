package pcp.tests;

import java.io.IOException;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.connectivity.ConnectivityChecker;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.generator.GraphProperties;
import pcp.generator.GraphType;
import pcp.generator.random.DimacsRandomPartitionedGraph;
import exceptions.AlgorithmException;

public class GraphGeneration {

	public static PartitionedGraph createPreprocessedGraph(int nodecount, int minpart, int maxpart, double density) throws AlgorithmException, IOException {
		PartitionedGraphBuilder builder = createBuilder(nodecount, minpart, maxpart, density);
		PartitionedGraph graph = new Preprocessor(builder).preprocess().getGraph();
		if (graph.N() == 0 || !new ConnectivityChecker(graph).check()) return null;
		return graph;
	}
	
	public static PartitionedGraph createGraph(int nodecount, int minpart, int maxpart, double density) throws AlgorithmException, IOException {
		PartitionedGraphBuilder builder = createBuilder(nodecount, minpart, maxpart, density);
		PartitionedGraph graph = builder.getGraph();
		if (graph.N() == 0 || !new ConnectivityChecker(graph).check()) return null;
		
		return graph;
	}

	private static PartitionedGraphBuilder createBuilder(int nodecount,
			int minpart, int maxpart, double density) {
		GraphProperties props = new GraphProperties();
		props.setBase(0);
		props.setEdgeProb(density);
		props.setMaxPartition(maxpart);
		props.setMinPartition(minpart);
		props.setName("test");
		props.setNodeCount(nodecount);
		props.setType(GraphType.Random);
		
		DimacsRandomPartitionedGraph dimacs = new DimacsRandomPartitionedGraph(props);
		PartitionedGraphBuilder builder = dimacs.getGraphBuilder();
		return builder;
	}
}
