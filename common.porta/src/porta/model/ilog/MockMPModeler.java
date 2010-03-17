package porta.model.ilog;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;

import java.util.ArrayList;
import java.util.List;

import porta.BaseParameters;
import porta.interfaces.IFactory;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.Model;
import porta.model.BaseVariable;
import porta.model.ilog.base.BaseMPModeler;
import porta.processing.ITranslator;


public class MockMPModeler extends BaseMPModeler {
	
	List<MockRange> constraints;
	IFactory factory;
	
	public MockMPModeler(IFactory factory) {
		this.constraints = new ArrayList<MockRange>();
		this.factory = factory;
	}
	
	public <C extends BaseConstraint, F extends BaseFamily, I extends BaseParameters> Model<C,F,I> asModel(ITranslator t, I cardinals) {
		Model<C, F, I> model = factory.createModel(cardinals);
		
		for (MockRange range : constraints) {
			BaseConstraint constraint = model.createConstraint(range.compare, (int)range.bound);
			for (MockLinearTerm term : range.expression.terms) {
				BaseVariable var = t.convertNameToModel(term.getVar().name);
				constraint.withVar(var, term.coef);
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
