package pcp.interfaces;

import ilog.concert.IloNumVar;

public interface IPrimalSolutionProvider {

	double[] getPrimalVals();
	IloNumVar[] getPrimalVars();
	Integer getPrimalChi();
	void clearPrimal();
	
}
