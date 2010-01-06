package pcp.interfaces;

import pcp.entities.Edge;
import pcp.entities.Node;

public interface IGraph {
	
	Node[] getNodes();
	
	boolean areAdjacent(Node n1, Node n2);
	
	String getName();
	
	Node[] getNeighbours(Node node);

	Edge[] getEdges();
	
	Node getNode(int index);
	
	int N();
	
	int E();
	
}