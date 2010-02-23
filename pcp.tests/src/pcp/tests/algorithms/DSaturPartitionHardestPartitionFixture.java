package pcp.tests.algorithms;

import pcp.algorithms.coloring.Coloring;
import pcp.algorithms.coloring.DSaturPartitionColoringHardestPartition;

public class DSaturPartitionHardestPartitionFixture extends DSaturPartitionFixture {

	@Override
	protected Coloring createDSatur() {
		DSaturPartitionColoringHardestPartition dsatur = new DSaturPartitionColoringHardestPartition(graph);
		return dsatur;
	}
	
}
