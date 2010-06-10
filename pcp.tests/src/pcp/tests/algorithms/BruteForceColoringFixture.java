package pcp.tests.algorithms;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.algorithms.coloring.BruteForcePartitionColoring;
import pcp.algorithms.coloring.ColoringVerifier;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import exceptions.AlgorithmException;

public class BruteForceColoringFixture {

	BruteForcePartitionColoring coloring;
	PartitionedGraphBuilder builder;
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
	}
	
	@Test
	public void testFourNodesSingleColor() throws AlgorithmException {
		builder
			.addNodesInPartition(0, 0,1)
			.addNodesInPartition(1, 2,3)
			.addEdges(0, 2,3)
			.addEdges(1, 3);
		
		test(1);
	}
	
	@Test
	public void testFourNodesTwoColors() throws AlgorithmException {
		builder
			.addNodesInPartition(0, 0,1)
			.addNodesInPartition(1, 2,3)
			.addEdges(0, 2,3)
			.addEdges(1, 2,3);
		
		test(2);
	}
	
	@Test
	public void testTenNodes() throws AlgorithmException {
		builder
			.addNodesInPartition(0, 0,1,2,3)
			.addNodesInPartition(1, 4,5,6)
			.addNodesInPartition(2, 7,8)
			.addNodesInPartition(3, 9,10)
			
			.addEdges(0, 4,5,6,7,8,9,10)
			.addEdges(1, 4,5,7,9,10)
			.addEdges(2, 4,5,7,8,9)
			.addEdges(3, 4,5,6,7,8)
			.addEdges(4, 7,8,9,10)
			.addEdges(5, 7,8,9,10)
			.addEdges(6, 7,8,9,10);
		
		test(2);
	}
	
	private void test(Integer colors) throws AlgorithmException {
		PartitionedGraph graph = builder.getGraph();
		coloring = new BruteForcePartitionColoring(graph);
		new ColoringVerifier(graph).verify(coloring).print(coloring);
		Assert.assertEquals(colors, coloring.getChi());
	}
	
}
