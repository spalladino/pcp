package pcp.tests.algorithms;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;

public class DSaturPartitionEasiestNodesFixture extends DSaturPartitionFixture {

	@Override
	protected ColoringAlgorithm createDSatur() {
		DSaturPartitionColoringEasiestNodes dsatur = new DSaturPartitionColoringEasiestNodes(graph);
		return dsatur;
	}
	
}
