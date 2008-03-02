package pcp.entities;

import pcp.entities.simple.SimpleEdge;
import pcp.entities.simple.SimpleNode;
import pcp.interfaces.IGraph;

public interface ISimpleGraph extends IGraph {
	
	SimpleEdge[] getEdges();
	
	SimpleNode[] getNodes();
	
	boolean areAdjacent(SimpleNode n1, SimpleNode n2);
	
	SimpleNode getNode(int index);
	
	int getDegree(SimpleNode node);
	
	SimpleNode[] getNeighbours(SimpleNode simpleNode);
}