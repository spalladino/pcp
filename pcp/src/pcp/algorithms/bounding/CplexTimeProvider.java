package pcp.algorithms.bounding;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import pcp.Logger;
import pcp.interfaces.ITimeProvider;

public class CplexTimeProvider implements ITimeProvider{

	private IloCplex cplex;

	public CplexTimeProvider(IloCplex cplex) {
		this.cplex = cplex;
	}

	@Override
	public double getTime()  {
		try {
			return cplex.getCplexTime();
		} catch (IloException e) {
			Logger.error("Error getting cplex time", e);
			return 0.0;
		}
	}
	
}
