package pcp.porta.model;



public class Variable {

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
}
