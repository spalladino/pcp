package pcp.tests.algorithms;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pcp.algorithms.Preprocessor;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import props.Settings;

public class PreprocessorFixture {

	public PreprocessorFixture() throws Exception {
		Settings.load("test");
	}
	
	Preprocessor preprocessor;
	PartitionedGraphBuilder builder;
	PartitionedGraph graph;
	
	@Before
	public void setup() {
		builder = new PartitionedGraphBuilder("test");
		preprocessor = new Preprocessor(builder);
	}
	
	@Test
	public void testVoidGraph() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		
		builder.addEdge(0, 1);
		builder.addEdge(0, 2);
		builder.addEdge(2, 4);
		builder.addEdge(3, 4);

		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(0, 0, 0);
	}
	
	@Test
	public void testNoChanges() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		builder.addNode(5, 1);
		
		builder.addEdge(0, 3);
		builder.addEdge(1, 4);
		builder.addEdge(2, 5);

		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(6, 3, 2);
	}
	
	@Test
	public void testRemoveEdges() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		builder.addNode(5, 1);
		
		builder.addEdge(0, 3);
		builder.addEdge(1, 4);
		builder.addEdge(2, 5);

		builder.addEdge(0, 1);
		builder.addEdge(0, 2);
		builder.addEdge(3, 4);

		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(6, 3, 2);
	}
	
	@Test
	public void testRemovePartition() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		builder.addNode(5, 2);
		builder.addNode(6, 2);
		
		builder.addEdge(3, 5);
		builder.addEdge(4, 6);
		
		builder.addEdge(0, 1);
		builder.addEdge(0, 2);
		builder.addEdge(3, 4);

		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(4, 2, 2);
	}
	
	@Test
	public void testRemoveNodes() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		builder.addNode(5, 1);
		
		builder.addNode(6, 2);
		builder.addNode(7, 2);
		builder.addNode(8, 2);
		
		// From P0 node 2 is removed
		builder.addEdge(0, 7);
		builder.addEdge(1, 8);
		builder.addEdge(2, 6);
		builder.addEdge(2, 7);
		builder.addEdge(2, 8);
		
		// From P1 node 5 is removed
		builder.addEdge(3, 6);
		builder.addEdge(4, 7);
		builder.addEdge(5, 6);
		
		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(7, 4, 3);
	}
	
	@Test
	public void testRemoveNodesSubsumption() {
		builder
			.addNodesInPartition(0, 0,1)
			.addNodesInPartition(1, 2)
			.addNodesInPartition(2, 3,7)
			.addNodesInPartition(3, 4,8)
			.addNodesInPartition(4, 5,6)
			.addEdges(0, 2,3,4)
			.addEdges(1, 3,4,5)
			.addEdges(7, 8);
			
		graph = preprocessor.preprocess().getGraph();
		assertCounts(5, 3, 3);
	}
	
	@Test
	public void testRemoveNodesVoidGraph() {
		
		builder.addNode(0, 0);
		builder.addNode(1, 0);
		builder.addNode(2, 0);
		builder.addNode(3, 1);
		builder.addNode(4, 1);
		
		builder.addEdge(0, 3);
		builder.addEdge(1, 3);
		builder.addEdge(1, 4);
		builder.addEdge(2, 3);
		builder.addEdge(2, 4);

		graph = preprocessor.preprocess().getGraph();
		
		assertCounts(0, 0, 0);
	}
	
	private void assertCounts(int nodes, int edges, int partitions) {
		graph = builder.getGraph();
		assertEquals(nodes, graph.getNodes().length);
		assertEquals(edges, graph.getEdges().length);
		assertEquals(partitions, graph.getPartitions().length);
	}
	
}
