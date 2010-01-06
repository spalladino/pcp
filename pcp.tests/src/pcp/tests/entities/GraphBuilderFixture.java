package pcp.tests.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pcp.entities.PartitionedGraph;
import pcp.entities.PartitionedGraphBuilder;


public class GraphBuilderFixture {
	
	PartitionedGraphBuilder builder;
	PartitionedGraph graph;
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		
		builder.addEdge(0, 1);
		builder.addEdge(0, 2);
		builder.addEdge(2, 4);
		builder.addEdge(3, 4);
	}
	
	@Test
	public void testRemovePartition() {
		builder.removePartition(builder.getPartitions()[0]);
		PartitionedGraph graph = builder.getGraph();

		assertCounts(2, 1, 1);
		
		assertEquals(0, graph.getPartitions()[0].getNodes()[0].index());
		assertEquals(1, graph.getPartitions()[0].getNodes()[1].index());
		
		assertEquals(0, graph.getEdges()[0].getNode1().index());
		assertEquals(1, graph.getEdges()[0].getNode2().index());
	}
	
	@Test
	public void testRemoveSecondPartition() {
		builder.removePartition(builder.getPartitions()[1]);
		assertCounts(3, 2, 1);
	}
	
	@Test
	public void testRemoveNode() {
		builder.removeNode(builder.getNodes()[3]);
		assertCounts(4, 3, 2);
		assertEquals(true, graph.areAdjacent(graph.getNodes()[2], graph.getNodes()[3]));
	}

	@Test
	public void testRemoveNodesInPartition() {
		builder.removeNode(builder.getNodes()[3]);
		builder.removeNode(builder.getNodes()[3]);
		assertCounts(3, 2, 1);
	}
	
	@Test
	public void testRemoveEdge() {
		builder.removeEdge(builder.getEdges()[0]);
		assertCounts(5, 3, 2);
		assertEquals(false, graph.areAdjacent(graph.getNodes()[0], graph.getNodes()[1]));
	}
	
	private void assertCounts(int nodes, int edges, int partitions) {
		graph = builder.getGraph();
		assertEquals(nodes, graph.getNodes().length);
		assertEquals(edges, graph.getEdges().length);
		assertEquals(partitions, graph.getPartitions().length);
	}
	
}
