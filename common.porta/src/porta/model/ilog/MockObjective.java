package porta.model.ilog;

import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloObjectiveSense;
import ilog.concert.IloCopyManager.Check;


public class MockObjective implements ilog.concert.IloObjective {
	
	@Override
	public void clearExpr() throws IloException {
	}
	
	@Override
	public IloNumExpr getExpr() throws IloException {
		return null;
	}
	
	@Override
	public IloObjectiveSense getSense() throws IloException {
		return null;
	}
	
	@Override
	public void setExpr(IloNumExpr arg0) throws IloException {
		
	}
	
	@Override
	public void setSense(IloObjectiveSense arg0) throws IloException {
		
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void setName(String arg0) {
		
	}
	
	@Override
	public IloCopyable makeCopy(IloCopyManager arg0) throws IloException {
		return null;
	}
	
	@Override
	public void needCopy(Check arg0) throws Check {
		
	}
	
}
