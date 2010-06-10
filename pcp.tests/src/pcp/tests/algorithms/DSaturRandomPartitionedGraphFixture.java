package pcp.tests.algorithms;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import pcp.algorithms.Preprocessor;
import pcp.algorithms.coloring.BruteForcePartitionColoring;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.algorithms.connectivity.ConnectivityChecker;
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
	BruteForcePartitionColoring exact;
	
	public DSaturRandomPartitionedGraphFixture() throws Exception {
		Settings.load("test");
	}
	
	@Test
	public void testUnpartitionedSmalls() throws AlgorithmException, IOException {
		testMultiple(1000, 10, 1, 1, 0.5);
	}
	
	@Test
	public void testVerySmalls() throws AlgorithmException, IOException {
		testMultiple(1000, 7, 2, 3, 0.5);
	}
	
	@Test
	public void testSmalls() throws AlgorithmException, IOException {
		testMultiple(100, 10, 2, 3, 0.5);
	}
	
	@Test
	public void testMediums() throws AlgorithmException, IOException {
		testMultiple(100, 20, 2, 3, 0.5);
	}

	
	protected void testMultiple(int iters, int nodecount, int minpart, int maxpart, double density) throws AlgorithmException, IOException {
		for (int i = 0; i < iters; i++) {
			test(nodecount, minpart, maxpart, density);
		}
	}
	
	protected void test(int nodecount, int minpart, int maxpart, double density) throws AlgorithmException, IOException {
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
		graph = new Preprocessor(builder).preprocess().getGraph();
		if (graph.N() == 0 || !new ConnectivityChecker(graph).check()) return;
		
		System.out.println("\n\nNew graph:\n");
		graph.print(System.out);
		
		check();
	}
	
	protected void check() throws AlgorithmException {
		dsatur = createDSatur();
		exact = createExact();
		
		System.out.println();
		new ColoringVerifier(graph)
			.verify(dsatur)
			.verify(exact);
		
		System.out.println("\n" + exact.getColorClassString());
		Assert.assertEquals(exact.getChi(), dsatur.getChi());
	}
	
	private BruteForcePartitionColoring createExact() {
		return new BruteForcePartitionColoring(graph);
	}

	protected ColoringAlgorithm createDSatur() {
		 return new DSaturPartitionColoringEasiestNodes(graph);
	}

	
}
