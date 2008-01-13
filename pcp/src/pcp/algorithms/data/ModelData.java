package pcp.algorithms.data;

import pcp.interfaces.IModelData;


public class ModelData implements IModelData {

	protected double[] ws;
	protected double[][] xs;

	public ModelData() {
	}
	
	public ModelData(double[][] xs, double[] ws) {
		this.xs = xs;
		this.ws = ws;
	}

	@Override
	public double w(int j) {
		return ws[j];
	}

	@Override
	public double[] ws() {
		return ws;
	}

	@Override
	public double x(int i, int j) {
		return xs[i][j];
	}

	@Override
	public double[] xs(int node) {
		return xs[node];
	}
	
	public void setXs(double[][] xs) {
		this.xs = xs;
	}
	
	public void setWs(double[] ws) {
		this.ws = ws;
	}
	
}
