package pcp.interfaces;

import pcp.entities.Node;
import pcp.entities.Partition;

public interface IPartitionedGraph extends IGraph {
	
	boolean areInSamePartition(Node n1, Node n2);
	
	Partition[] getPartitions();
	
	Node[] getNodes(Partition partition);
	
	Partition getPartition(Node node);

	int getDegree(Node node);
	
	int P();
}