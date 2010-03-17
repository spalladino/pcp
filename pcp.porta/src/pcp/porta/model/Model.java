package pcp.porta.model;

import java.util.ArrayList;
import java.util.List;

import pcp.porta.Parameters;
import porta.model.BaseModel;


public class Model extends BaseModel<Constraint, Family, Parameters> {
	
	Variable[][] xs;
	Variable[] ws;
	
	List<Constraint> constraints;
	List<Family> families;
	
	Parameters p;
	
	public Model(Parameters p) {
		super(p);
		this.p = p;
		
		constraints = new ArrayList<Constraint>();
		families = new ArrayList<Family>();

		xs = new Variable[p.nodeCount][p.colorCount];
		ws = new Variable[p.colorCount];
		
		for (int i = 0; i < xs.length; i++) {
			for (int j = 0; j < xs[i].length; j++) {
				xs[i][j] = new Variable(i, j);
			}
		}
	
		for (int j = 0; j < ws.length; j++) {
			ws[j] = new Variable(j);
		}
	}
	
	public Constraint createConstraint() {
		Constraint c = new Constraint(p.nodeCount, p.colorCount, 0, 0);
		constraints.add(c);
		return c;
	}
	
	public Constraint createConstraint(int compare, int bound) {
		Constraint c = new Constraint(p.nodeCount, p.colorCount, compare, bound);
		constraints.add(c);
		return c;
	}
	
	public Family createFamily(int compare, int bound) {
		Family c = new Family(p.nodeCount, p.colorCount, compare, bound);
		families.add(c);
		return c;
	}
	
	public Family createFamily(Constraint base) {
		Family c = new Family(p.nodeCount, p.colorCount, base.getCompare(), base.getBound());
		c.inner.xs = base.xs.clone();
		c.inner.ws = base.ws.clone();
		c.represented.add(base);
		families.add(c);
		return c;
	}
	
	public void removeFamily(Family f) {
		families.remove(f);
	}

	public Variable x(int i, int j) {
		return xs[i][j];
	}
	
	public Variable w(int j) {
		return ws[j];
	}
	
	public Parameters getCardinals() {
		return p;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}
	
	public List<Family> getFamilies() {
		return families;
	}
	
}
