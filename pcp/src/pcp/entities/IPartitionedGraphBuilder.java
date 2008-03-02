package pcp.entities;

import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;

public interface IPartitionedGraphBuilder {
	
	void removePartition(Partition rpartition);
	
	void removeNode(Node rnode);
	
	void removeEdge(Edge redge);
	
	IPartitionedGraph getGraph();
	
	IPartitionedGraphBuilder addNode(int node, int partition);
	
	IPartitionedGraphBuilder addEdge(int n1, int n2);
	
	boolean hasNode(int node);
	
	boolean hasPartition(int partition);
	
}