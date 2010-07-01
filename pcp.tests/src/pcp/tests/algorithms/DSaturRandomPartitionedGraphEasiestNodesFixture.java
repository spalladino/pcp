package pcp.tests.algorithms;

import org.junit.Test;

import exceptions.AlgorithmException;
import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;
import pcp.entities.partitioned.PartitionedGraphBuilder;

public class DSaturRandomPartitionedGraphEasiestNodesFixture extends DSaturRandomPartitionedGraphFixture {

	public DSaturRandomPartitionedGraphEasiestNodesFixture() throws Exception {
		super();
	}

	@Override
	protected ColoringAlgorithm createDSatur() {
		 return new DSaturPartitionColoringEasiestNodes(graph);
	}

	@Test
	public void testVerySmallFail() throws AlgorithmException {
		graph = new PartitionedGraphBuilder("test")
			.addNodesInPartition(0, 0)
			.addNodesInPartition(1, 1,2)
			.addNodesInPartition(2, 3)
			.addNodesInPartition(3, 4)
			
			.addEdges(0, 4)
			.addEdges(1, 4)
			.addEdges(2, 3)
			
			.getGraph();

		check();
	}
	
}
