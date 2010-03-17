package pcp.porta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pcp.utils.NameUtils;
import porta.interfaces.ITermWriter;
import porta.model.BaseConstraint;
import porta.model.BaseFamily;
import porta.model.BaseVariable;


public class Family extends BaseFamily {

	List<Constraint> represented;
	
	Set<Integer> colorValues;
	Set<Integer> nodeValues;
	
	boolean colorVariable;
	boolean nodeVariable;
	
	Constraint inner;
	
	public Family(int nodecount, int colorcount, int compare, int bound) {
		inner = new Constraint(nodecount, colorcount, compare, bound);
		this.represented = new ArrayList<Constraint>();
		this.colorValues = new TreeSet<Integer>();
		this.nodeValues = new TreeSet<Integer>();
	}
	
	public void withConstraint(Constraint c) {
		represented.add(c);
	}
	
	@Override
	public ITermWriter getTermWriter() {
		return new ITermWriter() {
			public String term(int coef, BaseVariable v) {
				Variable var = (Variable) v;
				Integer i = var.getNode();
				Integer j = var.getColor();
				String s = NameUtils.asCoef(coef);
				String strj = colorVariable ? "j" + NameUtils.plus(j) : String.valueOf(j);
				if (i == null) {
					return s + NameUtils.asVar(strj);
				} else {
					String stri = nodeVariable ? "i" + NameUtils.plus(i) : String.valueOf(i);
					return s + NameUtils.asVar(stri, strj);
				}
			}
		};
	}
	
	@Override
	protected void innerToString(ITermWriter termWriter, StringBuilder sb) {
		inner.innerToString(termWriter, sb);
		
		if (colorVariable && colorValues.size() > 0) {
			sb.append(" for j=");
			for (Integer c : colorValues) {
				sb.append(c).append(',');
			} 
		}
		if (nodeVariable && nodeValues.size() > 0) {
			sb.append(" for i=");
			for (Integer c : nodeValues) {
				sb.append(c).append(',');
			} 
		}
		
		if (represented.size() > 0) {
			sb.append(" (");
			for (Constraint r : represented) {
				sb.append(r.getIndex()).append(' ');
			} sb.append(")");
		}
	}

	
	@Override
	public BaseConstraint withVar(BaseVariable var, int coef) {
		inner.withVar(var, coef);
		return this;
	}

	@SuppressWarnings("unchecked")
	public List<BaseConstraint> getRepresented() {
		return (List) represented;
	}

	public boolean isColorVariable() {
		return colorVariable;
	}

	
	public void setColorVariable() {
		this.colorVariable = true;
	}

	
	public boolean isNodeVariable() {
		return nodeVariable;
	}

	
	public void setNodeVariable() {
		this.nodeVariable = true;
	}

	
	public Set<Integer> getColorValues() {
		return colorValues;
	}

	
	public Set<Integer> getNodeValues() {
		return nodeValues;
	}

	public boolean isOnlyColors() {
		return inner.isOnlyColors();
	}
	
}
