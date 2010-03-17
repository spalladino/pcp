package porta.model;

import java.util.ArrayList;
import java.util.List;

import porta.BaseParameters;


public abstract class Model<C extends BaseConstraint, F extends BaseFamily, I extends BaseParameters> {
	List<C> constraints;
	List<F> families;
	
	I p;
	
	public Model(I p) {
		this.p = p;
		
		constraints = new ArrayList<C>();
		families = new ArrayList<F>();
	}
	
	public abstract C createConstraint();
	
	@SuppressWarnings("unchecked")
	public C createConstraint(int compare, int bound) {
		return (C)createConstraint().withCompare(compare).withBound(bound);
	}
	
	public abstract BaseFamily createFamily(int compare, int bound);
	
	public abstract BaseFamily createFamily(BaseConstraint base);
	
	public void removeFamily(BaseFamily f) {
		families.remove(f);
	}

	public I getCardinals() {
		return p;
	}

	public List<C> getConstraints() {
		return constraints;
	}
	
	public List<F> getFamilies() {
		return families;
	}
	
}
