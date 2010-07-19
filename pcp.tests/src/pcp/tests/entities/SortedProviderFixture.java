package pcp.tests.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pcp.algorithms.data.StubModelData;
import pcp.common.sorting.SortedProvider;
import pcp.definitions.Sorting;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.entities.partitioned.SortedPartitionedGraph;


public class SortedProviderFixture {
	
	PartitionedGraph graph;
	StubModelData data;
	SortedProvider provider;
	
	@Before
	public void buildGraph() {
		graph = new PartitionedGraphBuilder("test")
			.addNodesInPartition(0, 0, 1, 2)
			.addNodesInPartition(1, 3)
			.addNodesInPartition(2, 4, 5)
			.addEdges(0, 3,4)
			.addEdges(5, 1,2)
			.addEdges(3, 4)
			.getGraph();
		
		data = new StubModelData(6)
			.withXs(0, 0.2, 0.4)
			.withXs(1, 0.3, 0.1)
			.withXs(2, 0.4, 0.4)
			.withXs(3, 0.95, 0.4)
			.withXs(4, 0.1, 0.5)
			.withXs(5, 0.25, 0.5)
			.withWs(1,1);
		
		provider = new SortedProvider(graph, 2, data);
	}
	
	@Test
	public void testSortedDescColorZero() {
		SortedPartitionedGraph sortedGraph = provider.getSortedGraph(0, Sorting.Desc);
		
		assertEquals(graph.getNode(3), sortedGraph.getNodes()[0]);
		assertEquals(graph.getNode(2), sortedGraph.getNodes()[1]);
		assertEquals(graph.getNode(1), sortedGraph.getNodes()[2]);
		assertEquals(graph.getNode(5), sortedGraph.getNodes()[3]);
		assertEquals(graph.getNode(0), sortedGraph.getNodes()[4]);
		assertEquals(graph.getNode(4), sortedGraph.getNodes()[5]);
		
		assertEquals(graph.getPartitions()[1], sortedGraph.getPartitions()[0]);
		assertEquals(graph.getPartitions()[0], sortedGraph.getPartitions()[1]);
		assertEquals(graph.getPartitions()[2], sortedGraph.getPartitions()[2]);
		
		assertEquals(graph.getNode(3), sortedGraph.getNodes(sortedGraph.getPartitions()[0])[0]);
		
		assertEquals(graph.getNode(2), sortedGraph.getNodes(sortedGraph.getPartitions()[1])[0]);
		assertEquals(graph.getNode(1), sortedGraph.getNodes(sortedGraph.getPartitions()[1])[1]);
		assertEquals(graph.getNode(0), sortedGraph.getNodes(sortedGraph.getPartitions()[1])[2]);
		
		assertEquals(graph.getNode(5), sortedGraph.getNodes(sortedGraph.getPartitions()[2])[0]);
		assertEquals(graph.getNode(4), sortedGraph.getNodes(sortedGraph.getPartitions()[2])[1]);

		assertEquals(0, sortedGraph.getEdges()[0].index1);
		assertEquals(3, sortedGraph.getEdges()[0].index2);
		assertEquals(3, sortedGraph.getEdges()[1].index1);
		assertEquals(4, sortedGraph.getEdges()[1].index2);
		assertEquals(5, sortedGraph.getEdges()[2].index1);
		assertEquals(2, sortedGraph.getEdges()[2].index2);
		assertEquals(5, sortedGraph.getEdges()[3].index1);
		assertEquals(1, sortedGraph.getEdges()[3].index2);
		assertEquals(0, sortedGraph.getEdges()[4].index1);
		assertEquals(4, sortedGraph.getEdges()[4].index2);

	}
	
	
	
}
