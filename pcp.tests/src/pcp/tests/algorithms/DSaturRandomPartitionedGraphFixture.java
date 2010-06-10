package pcp.tests.algorithms;

import org.junit.Assert;
import org.junit.Test;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.coloring.BruteForcePartitionColoring;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.generator.GraphProperties;
import pcp.generator.GraphType;
import pcp.generator.random.DimacsRandomPartitionedGraph;
import props.Settings;
import exceptions.AlgorithmException;

public class DSaturRandomPartitionedGraphFixture {

	PartitionedGraphBuilder builder;
	PartitionedGraph graph;
	
	ColoringAlgorithm dsatur;
	ColoringAlgorithm exact;
	
	public DSaturRandomPartitionedGraphFixture() throws Exception {
		Settings.load("test");
	}
	
	@Test
	public void testSmalls() throws AlgorithmException {
		testMultiple(100, 10, 2, 3, 0.5);
		
	}
	
	protected void testMultiple(int iters, int nodecount, int minpart, int maxpart, double density) throws AlgorithmException {
		for (int i = 0; i < iters; i++) {
			test(nodecount, minpart, maxpart, density);
		}
	}
	
	protected void test(int nodecount, int minpart, int maxpart, double density) throws AlgorithmException {
		GraphProperties props = new GraphProperties();
		props.setBase(0);
		props.setEdgeProb(density);
		props.setMaxPartition(maxpart);
		props.setMinPartition(minpart);
		props.setName("test");
		props.setNodeCount(nodecount);
		props.setType(GraphType.Random);
		
		PartitionedGraphBuilder builder = new DimacsRandomPartitionedGraph(props).getGraphBuilder();
		graph = new Preprocessor(builder).preprocess().getGraph();
		
		check();
	}
	
	protected void check() throws AlgorithmException {
		dsatur = createDSatur();
		exact = createExact();
		
		Assert.assertEquals(exact.getChi(), dsatur.getChi());
		new ColoringVerifier(graph)
			.verify(dsatur)
			.verify(exact);
	}
	
	private ColoringAlgorithm createExact() {
		return new BruteForcePartitionColoring(graph);
	}

	protected ColoringAlgorithm createDSatur() {
		 return new DSaturPartitionColoringEasiestNodes(graph);
	}

	
}
