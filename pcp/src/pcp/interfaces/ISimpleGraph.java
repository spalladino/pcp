package pcp.interfaces;

import pcp.entities.SimpleEdge;
import pcp.entities.SimpleNode;

public interface ISimpleGraph extends IGraph {
	
	SimpleEdge[] getEdges();
	
	SimpleNode[] getNodes();
	
	boolean areAdjacent(SimpleNode n1, SimpleNode n2);
	
	SimpleNode getNode(int index);
	
	int getDegree(SimpleNode node);
	
	SimpleNode[] getNeighbours(SimpleNode simpleNode);
}