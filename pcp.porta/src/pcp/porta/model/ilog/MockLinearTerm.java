package pcp.porta.model.ilog;

public class MockLinearTerm {
	
	int coef;
	MockIntVar var;
	
	public MockLinearTerm(MockIntVar var, int coef) {
		this.coef = coef;
		this.var = var;
	}
	
	public MockLinearTerm(int coef, MockIntVar var) {
		this.coef = coef;
		this.var = var;
	}

	public int getCoef() {
		return coef;
	}
	
	public MockIntVar getVar() {
		return var;
	}
	
	@Override
	public String toString() {
		if (coef == 0) return "+0";
		
		String c = "";
		if (coef == -1) c = "-";
		else if (coef < 0) c = "-" + String.valueOf(coef);
		else if (coef == 1) c = "+";
		else if (coef > 1) c = "+" + String.valueOf(coef);
		
		return c + var.toString();
	}
}
