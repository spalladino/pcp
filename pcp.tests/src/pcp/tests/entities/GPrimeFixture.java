package pcp.tests.entities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.entities.PartitionedGraphBuilder;
import pcp.interfaces.IPartitionedGraph;
import pcp.interfaces.ISimpleGraph;


public class GPrimeFixture {
	
	PartitionedGraphBuilder builder;
	IPartitionedGraph graph;
	ISimpleGraph gprime;
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
	}
	
	@Test
	public void test() {
		builder
			.addNodesInPartition(0, 1,2)
			.addNodesInPartition(1, 3,4,5)
			.addNodesInPartition(2, 6,7)
			.addNodesInPartition(3, 8)
			.addNodesInPartition(4, 0)
			
			.addEdges(1, 3,4,5,6,7,8)
			.addEdges(2, 3,4,5,6,7)

			.addEdges(6, 3,4,8)
			.addEdges(7, 5,8,0);
		
		graph = builder.getGraph();
		gprime = graph.getGPrime();
		
		Assert.assertEquals(5, gprime.N());
		Assert.assertEquals(3, gprime.E());
		
		Assert.assertTrue(gprime.areAdjacent(0, 1));
		Assert.assertTrue(gprime.areAdjacent(0, 2));
		Assert.assertTrue(gprime.areAdjacent(1, 0));
		Assert.assertTrue(gprime.areAdjacent(2, 0));
		Assert.assertTrue(gprime.areAdjacent(2, 3));
		Assert.assertTrue(gprime.areAdjacent(3, 2));
		
		Assert.assertFalse(gprime.areAdjacent(1, 2));
		Assert.assertFalse(gprime.areAdjacent(2, 1));
		Assert.assertFalse(gprime.areAdjacent(1, 3));
		Assert.assertFalse(gprime.areAdjacent(3, 1));
		Assert.assertFalse(gprime.areAdjacent(0, 3));
		Assert.assertFalse(gprime.areAdjacent(3, 0));
		
		Assert.assertFalse(gprime.areAdjacent(4, 3));
		Assert.assertFalse(gprime.areAdjacent(4, 2));
		Assert.assertFalse(gprime.areAdjacent(4, 1));
		Assert.assertFalse(gprime.areAdjacent(4, 0));
		
		Assert.assertEquals(2, gprime.getNode(0).getNeighbours().length);
		Assert.assertEquals(1, gprime.getNode(1).getNeighbours().length);
		Assert.assertEquals(2, gprime.getNode(2).getNeighbours().length);
		Assert.assertEquals(1, gprime.getNode(3).getNeighbours().length);
		Assert.assertEquals(0, gprime.getNode(4).getNeighbours().length);
	}
	
	
	
}
