package pcp.interfaces;

import pcp.entities.Edge;
import pcp.entities.Node;
import pcp.entities.Partition;

/**
 * Represents a graph with its nodes partitioned in sets.
 */
public interface IPartitionedGraph extends IGraph {
	
	/**
	 * Returns the set of edges in this graph.
	 * @return the set of edges in this graph.
	 */
	Edge[] getEdges();
	
	/**
	 * Returns the set of nodes in this graph.
	 * @return the set of nodes in this graph.
	 */
	Node[] getNodes();
	
	/**
	 * Returns whether two nodes are adjacent.
	 * @param n1 A node
	 * @param n2 A node
	 * @return whether the nodes are adjacent.
	 */
	boolean areAdjacent(Node n1, Node n2);
	
	/**
	 * Returns the set of neighbours of a node.
	 * @param node The node
	 * @return the set of neighbours of the node.
	 */
	Node[] getNeighbours(Node node);

	/**
	 * Returns a node given its index
	 * @param index index of the node to retrieve
	 * @return a node given its index
	 */
	Node getNode(int index);
	
	/**
	 * Returns the degree of a node, this is, the number of nodes it is adjacent to.
	 * @param node The node
	 * @return the degree of the node.
	 */
	int getDegree(Node node);
	
	/**
	 * Returns whether two nodes are in the same partition.
	 * @param n1 Node one
	 * @param n2 Node two
	 * @return true iif the two nodes are in the same partition.
	 */
	boolean areInSamePartition(Node n1, Node n2);
	
	/**
	 * Returns the array of partitions in the graph.
	 * @return the array of partitions in the graph.
	 */
	Partition[] getPartitions();
	
	/**
	 * Given a node, returns a set of all partitions to which it is adjacent. 
	 * This is, a partition p will be returned only if there is a node v in p adjacent to n. 
	 * @param n The node
	 * @return set of all partitions to which the node is adjacent.
	 */
	Partition[] getNeighbourPartitions(Node n);
	
	/**
	 * Given a partition, returns a set of all partitions to which it is adjacent. 
	 * This is, a partition q will be returned only if there is a node v in q adjacent to a node u in p. 
	 * @param p The partition
	 * @return set of all partitions to which the partition is adjacent.
	 */
	Partition[] getNeighbourPartitions(Partition p);
	
	/**
	 * Given a partition, returns a set of all nodes to which it is adjacent. 
	 * This is, a node v will be returned only if there is a node u in p adjacent to v. 
	 * @param p The partition
	 * @return set of all nodes to which it is adjacent.
	 */
	Node[] getNeighbours(Partition p);

	/**
	 * Returns all the nodes in a partition.
	 * @param partition The partition
	 * @return all the nodes in the partition.
	 */
	Node[] getNodes(Partition partition);
	
	/**
	 * Returns the partition the node belongs to.
	 * @param node Node
	 * @return the partition the node belongs to.
	 */
	Partition getPartition(Node node);
	
	/**
	 * Returns the number of partitions in the graph.
	 * @return
	 */
	int P();
	
	/**
	 * Returns a standard graph G'=<V',E'>, in which the set of nodes has one node for each
	 * partition in the graph G, and two nodes are adjacent iif both partitions formed a bipartite graph.
	 * @return the graph G' for this partitioned graph.
	 */
	IGraph getGPrime();
}