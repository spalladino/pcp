package porta.model;

import porta.interfaces.ITermWriter;



public abstract class BaseConstraint {
	
	protected int compare;
	protected int bound;
	protected String index;

	public BaseConstraint() {
		super();
	}

	public String getIndex() {
		return index;
	}
	
	public int getCompare() {
		return compare;
	}

	public int getBound() {
		return bound;
	}

	public BaseConstraint addBound(int bound) {
		this.bound += bound;
		return this;
	}

	public BaseConstraint withBound(int b) {
		this.bound = b;
		return this;
	}

	public BaseConstraint withCompare(int c) {
		this.compare = c;
		return this;
	}

	public BaseConstraint withIndex(String index) {
		this.index = index;
		return this;
	}
	
	public abstract BaseConstraint withVar(BaseVariable var, int coef);

	public void addVar(BaseVariable var, int coef) {
		withVar(var, coef);
	}
	
	@Override
	public String toString() {
		return toString(getTermWriter());
	}

	public String toString(final ITermWriter termWriter) {
		final StringBuilder sb = new StringBuilder();
		innerToString(termWriter, sb);
		return sb.toString();
	}

	protected abstract void innerToString(final ITermWriter termWriter, final StringBuilder sb);

	protected abstract ITermWriter getTermWriter();

	
}