package pcp.tests.algorithms;

import pcp.algorithms.coloring.ColoringAlgorithm;
import pcp.algorithms.coloring.DSaturPartitionColoringHardestPartition;

public class DSaturRandomPartitionedGraphHardestPartitionFixture extends DSaturRandomPartitionedGraphFixture {

	public DSaturRandomPartitionedGraphHardestPartitionFixture() throws Exception {
		super();
	}

	@Override
	protected ColoringAlgorithm createDSatur() {
		 return new DSaturPartitionColoringHardestPartition(graph);
	}

	
}
