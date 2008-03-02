package pcp.algorithms;

import static pcp.utils.ArrayUtils.containsSorted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pcp.common.sorting.NodeDegreeComparator;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraphBuilder;

/**
 * Preprocesses the input graph before solving the PCP problem. This includes
 * removing all edges within partitions, isolated nodes with their partitions,
 * and clearing empty partitions.
 */
public class Preprocessor {
	
	private PartitionedGraphBuilder builder;
	private int partitionsCheckCount;
	
	public Preprocessor(PartitionedGraphBuilder builder) {
		this.builder = builder;
		this.partitionsCheckCount = 0;
	}
	
	private static class PartitionData {
		
		public boolean check;
		
		public PartitionData(boolean check) {
			this.check = check;
		}
	}
	
	public PartitionedGraphBuilder preprocess() {
		// Remove edges in the same partition
		removeEdgesWithinPartition();
		
		// Remove all nodes with siblings whose neighbourhoods can be included
		// as well as all partitions that have nodes with no neighbourhood
		removeRedundantNodes();
		
		// Remove isolated partitions that might had been left.
		removePartitionsWithIsolatedNodes();
		
		// Return the builder for further processing
		return builder;
	}

	
	private void removeRedundantNodes() {
		// Setup partitions data
		for (Partition partition : builder.getPartitions()) {
			partition.setData(new PartitionData(true));
			partitionsCheckCount++;
		}
		
		// Iterate while there are changes in a partition
		while (partitionsCheckCount > 0) {
			
			// Go through the whole collection filtering out those not marked for check
			for (Partition partition : builder.getPartitions()) {
				if (partitionsCheckCount == 0) break;
				PartitionData data = getPartitionData(partition);
				if (!data.check) continue;
				
				// Mark this partition as processed
				data.check = false;
				partitionsCheckCount--;
				
				// Process the partition itself
				processPartition(partition);
			}
		}
	}

	private void processPartition(Partition partition) {
		// Setup structures
		Node[] sorted = partition.getNodes();
		Arrays.sort(sorted, new NodeDegreeComparator());
		List<Node[]> neighbourhoods = new ArrayList<Node[]>(sorted.length);
		
		// Iterate through nodes in degree ascending order
		for (int n = 0; n < sorted.length; n++) {
			Node node = sorted[n];
			Node[] neighbours = node.getNeighbours();
			
			// If there is an empty node, remove this partition
			if (neighbours.length == 0) {
				builder.removePartition(partition);
				break;
			}
			
			// Otherwise look for all previous sets created, if they can
			// be included in current neighbourhood
			boolean removed = false;
			for (Node[] existing : neighbourhoods) {
				if (containsSorted(neighbours, existing)) {
					markNeighbourPartitionsForCheck(neighbours);
					builder.removeNode(node);
					removed = true;
					break;
				}
			}
			
			// If we did not find a sibling that is included within the
			// current one, then add it to the set
			if (!removed) neighbourhoods.add(neighbours);
		}
	}
	
	private PartitionData getPartitionData(Partition p) {
		return (PartitionData) p.getData();
	}
	
	private void markNeighbourPartitionsForCheck(Node[] neighbours) {
		for (int i = 0; i < neighbours.length; i++) {
			Node node = neighbours[i];
			PartitionData data = getPartitionData(builder.getPartition(node)); 
			if (!data.check) {
				data.check = true;
				partitionsCheckCount++;
			}
		}
		
	}
	
	private void removePartitionsWithIsolatedNodes() {
		for (Node node : builder.getNodes()) {
			if (builder.hasNode(node.index())) {
				if (node.getNeighbours().length == 0) {
					builder.removePartition(node.getPartition());
				}
			}
		}
	}
	
	private void removeEdgesWithinPartition() {
		for (Edge edge : builder.getEdges()) {
			if (edge.getNode1().getPartition().index() == edge.getNode2().getPartition().index()) {
				builder.removeEdge(edge);
			}
		}
	}
	
}
