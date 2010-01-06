package pcp.interfaces;

import java.util.List;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.UnknownObjectException;

public interface ISolutionData {

	IPartitionedGraph getGraph();
	
	int getNodeColor(int node) throws IloException;

	double[] getColorValues() throws UnknownObjectException, IloException;

	double[] getNodeValues(int node) throws UnknownObjectException, IloException;

	List<Integer> getColorsUsed() throws IloException;
	
}
