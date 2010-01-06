package pcp.porta.model.ilog.base;

import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearIntExprIterator;
import ilog.concert.IloCopyManager.Check;


public class BaseLinearExpr implements IloLinearIntExpr {
	
	@Override
	public void add(IloLinearIntExpr arg0) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerm(int arg0, IloIntVar arg1) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerm(IloIntVar arg0, int arg1) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerms(int[] arg0, IloIntVar[] arg1) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerms(IloIntVar[] arg0, int[] arg1) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerms(int[] arg0, IloIntVar[] arg1, int arg2, int arg3) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addTerms(IloIntVar[] arg0, int[] arg1, int arg2, int arg3) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getConstant() throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IloLinearIntExprIterator linearIterator() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(IloIntVar arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void remove(IloIntVar[] arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void remove(IloIntVar[] arg0, int arg1, int arg2) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public void setConstant(int arg0) throws IloException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override
	public IloCopyable makeCopy(IloCopyManager arg0) throws IloException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void needCopy(Check arg0) throws Check {
		throw new UnsupportedOperationException();
		
	}
	
}
