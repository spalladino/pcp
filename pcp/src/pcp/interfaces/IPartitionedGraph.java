package pcp.interfaces;

import pcp.entities.Node;
import pcp.entities.Partition;

public interface IPartitionedGraph extends IGraph {
	
	boolean areInSamePartition(Node n1, Node n2);
	
	Partition[] getPartitions();
	
	Partition[] getNeighbourPartitions(Node n);
	
	Partition[] getNeighbourPartitions(Partition p);
	
	Node[] getNeighbours(Partition p);
	
	Node[] getNodes(Partition partition);
	
	Partition getPartition(Node node);
	
	int P();
}