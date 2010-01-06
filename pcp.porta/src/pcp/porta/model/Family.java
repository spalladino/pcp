package pcp.porta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pcp.utils.NameUtils;


public class Family extends Constraint {

	List<Constraint> represented;
	
	Set<Integer> colorValues;
	Set<Integer> nodeValues;
	
	boolean colorVariable;
	boolean nodeVariable;
	
	public Family(int nodecount, int colorcount, int compare, int bound) {
		super(nodecount, colorcount, compare, bound);
		this.represented = new ArrayList<Constraint>();
		this.colorValues = new TreeSet<Integer>();
		this.nodeValues = new TreeSet<Integer>();
	}
	
	public void representingConstraint(Constraint c) {
		represented.add(c);
	}
	
	@Override
	ITermWriter getTermWriter() {
		return new ITermWriter() {
			public String term(int coef, Integer i, Integer j) {
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
	void innerToString(ITermWriter termWriter, StringBuilder sb) {
		super.innerToString(termWriter, sb);
		
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
				sb.append(r.index).append(' ');
			} sb.append(")");
		}
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

	
	public List<Constraint> getRepresented() {
		return represented;
	}

	
	public Set<Integer> getColorValues() {
		return colorValues;
	}

	
	public Set<Integer> getNodeValues() {
		return nodeValues;
	}
	
}
