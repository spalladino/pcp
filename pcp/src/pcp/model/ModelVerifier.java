package pcp.model;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloLinearNumExprIterator;
import ilog.concert.IloModel;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pcp.utils.DoubleUtils;
import exceptions.AlgorithmException;

public class ModelVerifier {

	private IloModel model;
	private Map<IloNumVar, Double> values;
	
	private boolean throwExc = false;
	private boolean showValues = true;
	
	public ModelVerifier(IloModel model) {
		this.model = model;
	}
	
	@SuppressWarnings("unchecked")
	public void verify(IloNumVar[] vars, double[] vals) throws IloException, AlgorithmException {
		setValues(vars, vals);
		Iterator iter = model.iterator();
		List<IloRange> failed = new ArrayList<IloRange>();
		
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof IloRange) {
				IloRange range = (IloRange) obj;
				IloNumExpr expr = range.getExpr();
				if (expr instanceof IloLinearNumExpr) {
					IloLinearNumExpr linearExpr = (IloLinearNumExpr) expr;
					double value = evaluate(linearExpr);
					if (DoubleUtils.greaterThan(value, range.getUB()) ||
						DoubleUtils.lessThan(value, range.getLB())) {
						if (throwExc) throw new AlgorithmException("Expression value " + value + " is out of bounds: " + range);
						else failed.add(range);
					}
				}
			}
		}
		
		if (failed.size() > 0) {
			StringBuilder sb = new StringBuilder("Model verified: failed ");
			for(IloRange range : failed) {
				sb.append(range.getName()).append(", ");
				System.out.println(range);
			} System.out.println(sb);
		} else {
			System.out.println("Model successfully verified");
		}
	}

	private void setValues(IloNumVar[] vars, double[] vals) {
		StringBuilder vsb = new StringBuilder("Values: ");
		this.values = new HashMap<IloNumVar, Double>();
		for(int i = 0; i < vars.length; i++) {
			values.put(vars[i], vals[i]);
			vsb.append(vars[i].getName()).append("=").append(vals[i]).append(", ");
		}
		
		if (showValues) {
			System.out.println(vsb);
		}
	}
	
	private double evaluate(IloLinearNumExpr linearExpr) throws IloException, AlgorithmException {
		IloLinearNumExprIterator exprIter = linearExpr.linearIterator();
		double result = linearExpr.getConstant(); 
		while (exprIter.hasNext()) {
			IloNumVar numVar = exprIter.nextNumVar();
			double value = exprIter.getValue();
			result += getValue(numVar) * value;
		} return result;
	}
	
	private double getValue(IloNumVar var) {
		return values.get(var);
	}
	
}
