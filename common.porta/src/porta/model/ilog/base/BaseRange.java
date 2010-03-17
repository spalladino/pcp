package porta.model.ilog.base;

import ilog.concert.IloCopyManager;
import ilog.concert.IloCopyable;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.concert.IloCopyManager.Check;


public class BaseRange implements IloRange {

	@Override
	public void clearExpr() throws IloException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IloNumExpr getExpr() throws IloException {
		throw new UnsupportedOperationException();	}

	@Override
	public double getLB() throws IloException {
		throw new UnsupportedOperationException();	}

	@Override
	public double getUB() throws IloException {
		throw new UnsupportedOperationException();	}

	@Override
	public void setBounds(double arg0, double arg1) throws IloException {
		throw new UnsupportedOperationException();	}

	@Override
	public void setExpr(IloNumExpr arg0) throws IloException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLB(double arg0) throws IloException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUB(double arg0) throws IloException {
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

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setName(String arg0) {
		throw new UnsupportedOperationException();	}
	
}
