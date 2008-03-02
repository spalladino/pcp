package pcp.tests.entities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.InducedSimpleGraph;
import pcp.entities.simple.SimpleGraphBuilder;
import pcp.entities.simple.SimpleNode;


public class InducedSimpleGraphFixture {
	
	SimpleGraphBuilder builder;
	ISimpleGraph graph;
	ISimpleGraph induced;
	
	@Before
	public void setup() {
		builder = new SimpleGraphBuilder("test");
	}
	
	@Test
	public void test() {
		builder.addNodes(4)
			.addEdge(0, 1)
			.addEdge(1, 2)
			.addEdge(2, 3)
			.addEdge(3, 0)
			.addEdge(3, 1);
		
		graph = builder.getGraph();
		induced = new InducedSimpleGraph(graph, new SimpleNode[] {
			graph.getNode(0),
			graph.getNode(2),
			graph.getNode(3)
		});
		
		Assert.assertEquals(3, induced.N());
		
		Assert.assertTrue(induced.areAdjacent(0, 3));
		Assert.assertTrue(induced.areAdjacent(3, 0));
		Assert.assertTrue(induced.areAdjacent(3, 2));
		Assert.assertTrue(induced.areAdjacent(2, 3));
		
		Assert.assertFalse(induced.areAdjacent(0, 2));
		Assert.assertFalse(induced.areAdjacent(2, 0));
		
		Assert.assertEquals(1, induced.getNode(0).getNeighbours().length);
		Assert.assertEquals(1, induced.getNode(2).getNeighbours().length);
		Assert.assertEquals(2, induced.getNode(3).getNeighbours().length);
		
		Assert.assertEquals(1, induced.getNode(3).getNeighbours()[0].getNeighbours().length);
		Assert.assertEquals(1, induced.getNode(3).getNeighbours()[1].getNeighbours().length);
	}
	
	
}
