package pcp.interfaces;


public interface IGraph {
	
	String getName();
	
	int N();
	
	int E();
	
	boolean areAdjacent(int n1, int n2);
}