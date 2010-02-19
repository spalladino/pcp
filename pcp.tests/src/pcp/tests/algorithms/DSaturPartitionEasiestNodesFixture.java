package pcp.tests.algorithms;

import pcp.algorithms.coloring.Coloring;
import pcp.algorithms.coloring.DSaturPartitionColoringEasiestNodes;

public class DSaturPartitionEasiestNodesFixture extends DSaturPartitionFixture {

	@Override
	protected Coloring createDSatur() {
		DSaturPartitionColoringEasiestNodes dsatur = new DSaturPartitionColoringEasiestNodes(graph);
		return dsatur;
	}
	
}
