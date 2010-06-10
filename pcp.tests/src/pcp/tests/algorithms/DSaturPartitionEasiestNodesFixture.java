package pcp.tests.algorithms;

import org.junit.Test;

import exceptions.AlgorithmException;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;

public class DSaturPartitionEasiestNodesFixture extends DSaturPartitionFixture {

	@Override
	protected ColoringAlgorithm createDSatur() {
		DSaturPartitionColoringEasiestNodes dsatur = new DSaturPartitionColoringEasiestNodes(graph);
		return dsatur;
	}
	
	@Test
	public void testGraph01() throws AlgorithmException {
		builder
			.addNodesInPartition(0, 0,1)
			.addNodesInPartition(1, 2,3)
			.addNodesInPartition(2, 4)
			.addEdges(0, 2)
			.addEdges(1, 3)
			.addEdges(3, 4);
		
		check(1, 1);
	}
	
}
