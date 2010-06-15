package pcp.entities;

import java.io.PrintStream;

import pcp.entities.simple.Edge;
import pcp.entities.simple.Node;
import pcp.interfaces.IGraph;

public interface ISimpleGraph extends IGraph {
	
	Edge[] getEdges();
	
	Node[] getNodes();
	
	boolean areAdjacent(Node n1, Node n2);
	
	Node getNode(int index);
	
	int getDegree(Node node);
	
	Node[] getNeighbours(Node simpleNode);
	
	void print(PrintStream stream);
}