package pcp.solver.data;

import static pcp.utils.DoubleUtils.doubleEquals;
import static pcp.utils.DoubleUtils.isTrue;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import pcp.interfaces.ISolutionData;


public abstract class AbstractSolutionData implements ISolutionData {

	@Override
	public int getNodeColor(int node) throws IloException {
		double[] nodeColors = xs(node);
		for (int c = 0; c < nodeColors.length; c++) {
			if (doubleEquals(nodeColors[c], 1.0)) {
				return c;
			}
		} return -1;
	}
	
	@Override
	public List<Integer> getColorsUsed() throws IloException {
		List<Integer> used = new ArrayList<Integer>();
		double[] colorValues = ws();
		for (int c = 0; c < colorValues.length; c++) {
			if (isTrue(colorValues[c])) {
				used.add(c);
			}
		} return used;
	}
	
}
