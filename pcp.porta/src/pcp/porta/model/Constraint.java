package pcp.porta.model;

import java.util.ArrayList;
import java.util.List;

import pcp.common.Box;
import pcp.utils.NameUtils;

public class Constraint {
	
	int[][] xs;
	int[] ws;
	
	int compare;
	int bound;
	
	int nodeCount;
	int colorCount;
	String index;
	
	List<InequalityKind> ineqKinds;
	
	public Constraint(int nodecount, int colorcount, int compare, int bound) {
		this.compare = compare;
		this.bound = bound;
		this.nodeCount = nodecount;
		this.colorCount = colorcount;
		
		xs = new int[nodeCount][colorCount];
		ws = new int[colorCount];
		
		ineqKinds = new ArrayList<InequalityKind>(1);
	}
	
	public int getCompare() {
		return compare;
	}

	public int getBound() {
		return bound;
	}
	
	public List<InequalityKind> getIneqKinds() {
		return this.ineqKinds;
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

	public Constraint addBound(int bound) {
		this.bound += bound;
		return this;
	}
	
	public Constraint withBound(int b) {
		this.bound = b;
		return this;
	}
	
	public Constraint withCompare(int c) {
		this.compare = c;
		return this;
	}
	
	public Constraint withIndex(String index) {
		this.index = index;
		return this;
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

	void innerToString(final ITermWriter termWriter, final StringBuilder sb) {
		if (index != null) {
			sb.append('(').append(index).append(") ");
		}
		
		forXs(new INodeVarHandler() {
			public void handle(int i, int j, int coef) {
				sb.append(termWriter.term(coef, i, j)).append(' ');
			}
		});
		
		forWs(new IColorVarHandler() {
			public void handle(int j, int coef) {
				sb.append(termWriter.term(coef, null, j)).append(' ');
			}
		});
		
		sb.append(NameUtils.asCmp(compare)).append(' ').append(bound).append(' ');
		
		for (InequalityKind family : ineqKinds) {
			sb.append(family).append(' ');
		}
	}
	
	public static interface INodeVarHandler {
		void handle(int i, int j, int coef);
	}
	
	public static interface IColorVarHandler {
		void handle(int j, int coef);
	}
	
	public static interface ITermWriter {
		String term(int coef, Integer i, Integer j);
	}

	public static ITermWriter defaultTermWriter(final int base) {
		return new ITermWriter() {
			public String term(int coef, Integer i, Integer j) {
				if (i == null) return NameUtils.asTerm(coef, j+base);
				else return NameUtils.asTerm(coef, i+base, j+base);
			}
		};
	}
	
	ITermWriter getTermWriter() {
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

}
