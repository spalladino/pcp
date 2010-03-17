package porta.model.ilog;

import porta.model.ilog.base.BaseRange;


public class MockRange extends BaseRange  {
	
	MockLinearExpr expression;
	String name;
	int compare;
	double bound;
	
	public MockRange(MockLinearExpr expression, int compare, double bound, String name) {
		this.expression = expression;
		this.compare = compare;
		this.bound = bound;
		this.name = name;
	}

	@Override
	public MockLinearExpr getExpr() {
		return expression;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String cmp = "==";
		if (compare < 0) cmp = "<=";
		else if (compare > 0) cmp = ">=";
		
		String n = "";
		if (name != null && !name.isEmpty()) n = "(" + name + ") ";
		
		return n + expression.toString() + " " + cmp + " " + bound; 
	}
	
}
