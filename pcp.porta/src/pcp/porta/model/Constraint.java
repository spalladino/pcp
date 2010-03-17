package pcp.porta.model;

import pcp.utils.NameUtils;
import porta.interfaces.ITermWriter;
import porta.model.BaseConstraint;
import porta.model.BaseVariable;
import entities.Box;

public class Constraint extends BaseConstraint {
	
	int[][] xs;
	int[] ws;
	
	int nodeCount;
	int colorCount;
	
	public Constraint(int nodecount, int colorcount, int compare, int bound) {
		this.compare = compare;
		this.bound = bound;
		this.nodeCount = nodecount;
		this.colorCount = colorcount;
		
		xs = new int[nodeCount][colorCount];
		ws = new int[colorCount];
	}
	
	public int getNodeCount() {
		return nodeCount;
	}

	public int getColorCount() {
		return colorCount;
	}

	public int x(int i, int j) {
		return xs[i][j];
	}
	
	public int w(int j) {
		return ws[j];
	}

	public Constraint addVar(Integer i, Integer j, int coef) {
		if (i == null) return addW(j, coef);
		else return addX(i, j, coef);
	}
	
	public Constraint addX(int i, int j, int coef) {
		xs[i][j] += coef;
		return this;
	}
	
	public Constraint addW(int j, int coef) {
		ws[j] += coef;
		return this;
	}
	
	public void forXs(INodeVarHandler handler) {
		for (int i = 0; i < xs.length; i++) {
			for (int j = 0; j < xs[i].length; j++) {
				int coef = xs[i][j];
				if (coef != 0) {
					handler.handle(i, j, coef);
				}
			}
		}
	}
	
	public void forWs(IColorVarHandler handler) {
		for (int j = 0; j < ws.length; j++) {
			int coef = ws[j];
			if (coef != 0) {
				handler.handle(j, coef);
			}
		}
	}

	public boolean hasTerms() {
		final Box<Boolean> has = new Box<Boolean>(false);
		forXs(new INodeVarHandler() {
			public void handle(int i, int j, int coef) {
				has.setData(true);
			}
		});
		forWs(new IColorVarHandler() {
			public void handle(int j, int coef) {
				has.setData(true);
			}
		});
		return has.getData();
	}
	
	public boolean isOnlyColors() {
		final Box<Boolean> hasX = new Box<Boolean>(false);
		forXs(new INodeVarHandler() {
			public void handle(int i, int j, int coef) {
				hasX.setData(true);
			}
		});
		return !hasX.getData();
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

	@Override
	protected void innerToString(final ITermWriter termWriter, final StringBuilder sb) {
		if (index != null) {
			sb.append('(').append(index).append(") ");
		}
		
		forXs(new INodeVarHandler() {
			public void handle(int i, int j, int coef) {
				sb.append(termWriter.term(coef, new Variable(i, j))).append(' ');
			}
		});
		
		forWs(new IColorVarHandler() {
			public void handle(int j, int coef) {
				sb.append(termWriter.term(coef, new Variable(j))).append(' ');
			}
		});
		
		if (!hasTerms()) {
			sb.append("0 ");
		}
		
		sb.append(NameUtils.asCmp(compare)).append(' ').append(bound).append(' ');
	}
	
	public static interface INodeVarHandler {
		void handle(int i, int j, int coef);
	}
	
	public static interface IColorVarHandler {
		void handle(int j, int coef);
	}

	public static ITermWriter defaultTermWriter(final int base) {
		return new ITermWriter() {
			public String term(int coef, BaseVariable v) {
				Variable var = (Variable)v;
				if (var.node == null) return NameUtils.asTerm(coef, var.color+base);
				else return NameUtils.asTerm(coef, var.node+base, var.color+base);
			}
		};
	}
	
	protected ITermWriter getTermWriter() {
		return defaultTermWriter(1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bound;
		result = prime * result + colorCount;
		result = prime * result + compare;
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + nodeCount;
		return result;
	}

	@Override
	public BaseConstraint withVar(BaseVariable var, int coef) {
		Variable v = (Variable)var;
		return this.addVar(v.node, v.color, coef);
	}

}
