package pcp.porta.model.ilog;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;

import java.util.ArrayList;
import java.util.List;

import pcp.common.TupleInt;
import pcp.porta.model.Constraint;
import pcp.porta.model.Model;
import pcp.porta.model.ilog.base.BaseMPModeler;
import pcp.porta.processing.Translator;


public class MockMPModeler extends BaseMPModeler {
	
	List<MockRange> constraints;
	
	public MockMPModeler() {
		constraints = new ArrayList<MockRange>();
	}
	
	public Model asModel(Translator t) {
		Model model = new Model(t.getCardinals());
		
		for (MockRange range : constraints) {
			Constraint constraint = model.createConstraint(range.compare, (int)range.bound);
			for (MockLinearTerm term : range.expression.terms) {
				TupleInt nodeColor = t.convertNameToNodeColor(term.getVar().name);
				constraint.addVar(nodeColor.getFirst(), nodeColor.getSecond(), term.coef);
			}
		}
		
		return model;
	}
	
	@Override
	public MockIntVar boolVar() throws IloException {
		return new MockIntVar(null);
	}
	
	@Override
	public MockIntVar boolVar(String name) throws IloException {
		return new MockIntVar(name);
	}
	
	@Override
	public MockLinearExpr linearIntExpr() throws IloException {
		return new MockLinearExpr();
	}
	
	@Override
	public MockObjective addMinimize(IloNumExpr arg0) throws IloException {
		return new MockObjective();
	}
	
	@Override
	public MockRange addLe(IloNumExpr expr, double bound, String name) throws IloException {
		return addConstraint(expr, -1, bound, name);		
	}
	
	@Override
	public MockRange addGe(IloNumExpr arg0, double arg1, String arg2) throws IloException {
		return addConstraint(arg0, 1, arg1, arg2);
	}
	
	@Override
	public MockRange addEq(IloNumExpr arg0, double arg1, String arg2) throws IloException {
		return addConstraint(arg0, 0, arg1, arg2);
	}
	
	private MockRange addConstraint(IloNumExpr expr, int compare, double bound, String name) {
		MockRange c = new MockRange((MockLinearExpr)expr, compare, bound, name);
		constraints.add(c);
		return c;
	}

}
