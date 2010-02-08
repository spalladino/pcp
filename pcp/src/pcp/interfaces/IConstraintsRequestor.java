package pcp.interfaces;

import ilog.concert.IloConstraint;


public interface IConstraintsRequestor {
	
	boolean addInitialCut(IloConstraint constraint);
	
}
