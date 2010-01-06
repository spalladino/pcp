package pcp.porta.model.ilog.base;

import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloCopyManager.Check;


public class BaseIntVar implements IloIntVar {
	
	@Override
	public int getMax() throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getMin() throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setMax(int arg0) throws IloException {
		throw new UnsupportedOperationException();	
	}
	
	@Override
	public void setMin(int arg0) throws IloException {
		throw new UnsupportedOperationException();		
	}
	
	@Override
	public IloCopyable makeCopy(IloCopyManager arg0) throws IloException {
		throw new UnsupportedOperationException();	}
	
	@Override
	public void needCopy(Check arg0) throws Check {
		throw new UnsupportedOperationException();		
	}
	
	@Override
	public double getLB() throws IloException {
		throw new UnsupportedOperationException();	}
	
	@Override
	public String getName() {
		throw new UnsupportedOperationException();	}
	
	@Override
	public IloNumVarType getType() throws IloException {
		throw new UnsupportedOperationException();	}
	
	@Override
	public double getUB() throws IloException {
		throw new UnsupportedOperationException();	}
	
	@Override
	public void setLB(double arg0) throws IloException {
		throw new UnsupportedOperationException();	}
	
	@Override
	public void setName(String arg0) {
		throw new UnsupportedOperationException();		
	}
	
	@Override
	public void setUB(double arg0) throws IloException {
		throw new UnsupportedOperationException();	
	}
	
}
