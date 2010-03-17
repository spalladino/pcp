package porta.model;

import java.util.ArrayList;
import java.util.List;

import porta.base.BaseParameters;


public abstract class BaseModel<C extends BaseConstraint, F extends BaseFamily, I extends BaseParameters> {
	List<C> constraints;
	List<F> families;
	
	I p;
	
	public BaseModel(I p) {
		this.p = p;
		
		constraints = new ArrayList<C>();
		families = new ArrayList<F>();
	}
	
	public abstract C createConstraint();
	
	@SuppressWarnings("unchecked")
	public C createConstraint(int compare, int bound) {
		return (C)createConstraint().withCompare(compare).withBound(bound);
	}
	
	public abstract F createFamily(int compare, int bound);
	
	public abstract F createFamily(C base);
	
	public void removeFamily(F f) {
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
