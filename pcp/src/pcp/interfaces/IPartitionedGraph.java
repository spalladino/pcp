package pcp.interfaces;

import pcp.entities.Node;
import pcp.entities.Partition;

public interface IPartitionedGraph extends IGraph {
	
	Partition[] getPartitions();
	
	Node[] getNodes(Partition partition);
	
	Partition getPartition(Node node);

	int getDegree(Node node);
	
	int P();
}