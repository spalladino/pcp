package pcp.tests.algorithms;

import junit.framework.Assert;

import org.junit.Test;

import pcp.algorithms.clique.CliqueCover;
import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;
import pcp.entities.partitioned.InducedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.PartitionedGraphBuilder;
import pcp.interfaces.IGraph;


public class ComplexFixture {
	
	IPartitionedGraph graph;
	
	@Test
	public void testCliqueCoverOnNeighbourhoodInducedGPrime() {
		graph = new PartitionedGraphBuilder("test")
			.addNodesInPartition(0, 0,1)
			.addNodesInPartition(1, 2,3)
			.addNodesInPartition(2, 4,5)
			.addEdges(0, 2,4)
			.addEdges(1, 3,4,5)
			.addEdges(3, 4,5);
		
		IPartitionedGraph[] induced = induceFromNeighbourhoods(graph.getNodes());
		assertNodeCounts(induced, 2,3,1,3,3,2);
		
		ISimpleGraph[] gprimes = generateGPrimes(induced);
		assertNodeCounts(gprimes, 2,2,1,2,2,2);
		assertCliqueCovers(gprimes, 2,1,1,1,2,1);
	}
	
	private IPartitionedGraph[] induceFromNeighbourhoods(Node[] sources) {
		IPartitionedGraph[] induced = new IPartitionedGraph[sources.length];
		for (int i = 0; i < sources.length; i++) {
			induced[i] = new InducedGraph(graph, sources[i].getNeighbours());
		} return induced;
	}
	
	private void assertNodeCounts(IGraph[] graphs, int... counts) {
		for (int i = 0; i < graphs.length; i++) {
			Assert.assertEquals(counts[i], graphs[i].N());
		}
	}
	
	private ISimpleGraph[] generateGPrimes(IPartitionedGraph[] graphs) {
		ISimpleGraph[] gprimes = new ISimpleGraph[graphs.length];
		for (int i = 0; i < graphs.length; i++) {
			gprimes[i] = graphs[i].getGPrime();
		} return gprimes;
	}
	
	private void assertCliqueCovers(ISimpleGraph[] graphs, int... counts) {
		for (int i = 0; i < graphs.length; i++) {
			Assert.assertEquals(counts[i], new CliqueCover(graphs[i]).count());
		}
	}

}
