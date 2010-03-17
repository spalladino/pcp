package pcp.porta.model;

import porta.model.BaseVariable;

public class Variable extends BaseVariable {

	Integer color;
	Integer node;
	
	public Variable(Integer node, Integer color) {
		this.node = node;
		this.color = color;
	}
	
	public Variable(Integer color) {
		this.color = color;
	}

	@Override
	public String toString() {
		if (node == null) return String.format("w[%1$d]", color);
		else return String.format("x[%1$d,%2$d]", node, color);

	}

	public Integer getColor() {
		return color;
	}
	
	public Integer getNode() {
		return node;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}
}
