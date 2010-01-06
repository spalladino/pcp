package pcp.porta.model.ilog;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import pcp.porta.model.ilog.base.BaseLinearExpr;

public class MockLinearExpr extends BaseLinearExpr {
	
	List<MockLinearTerm> terms;
	
	public MockLinearExpr() {
		this.terms = new ArrayList<MockLinearTerm>();
	}
	
	@Override
	public void addTerm(IloIntVar arg0, int arg1) throws IloException {
		if (arg0 == null) throw new IloException("Variable cannot be null");
		terms.add(new MockLinearTerm(arg1, (MockIntVar)arg0));
	}

	public List<MockLinearTerm> getTerms() {
		return terms;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (MockLinearTerm term : terms) {
			builder.append(term.toString()).append(' ');
		} return builder.toString();
	}
}
