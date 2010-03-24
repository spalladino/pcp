package pcp.tests.algorithms;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.DSaturPartitionColoringHardestPartition;

public class DSaturPartitionHardestPartitionFixture extends DSaturPartitionFixture {

	@Override
	protected ColoringAlgorithm createDSatur() {
		DSaturPartitionColoringHardestPartition dsatur = new DSaturPartitionColoringHardestPartition(graph);
		return dsatur;
	}
	
}
