package pcp.interfaces;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.UnknownObjectException;

public interface IModelData {

	double w(int j) throws UnknownObjectException, IloException;

	double x(int i, int j) throws UnknownObjectException, IloException;
	
	double[] getColorValues() throws UnknownObjectException, IloException;

	double[] getNodeValues(int node) throws UnknownObjectException, IloException;
	
}
