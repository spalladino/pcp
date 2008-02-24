package pcp.interfaces;


public interface ISimpleGraphBuilder {
	
	ISimpleGraphBuilder addNode(int node);
	
	ISimpleGraphBuilder addEdge(int n1, int n2);
	
	ISimpleGraph getGraph();
	
}
