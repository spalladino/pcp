package pcp.algorithms.data;


public class StubModelData extends ModelData {
	
	int nodes;

	public StubModelData(int nodes) {
		this.nodes = nodes;
		this.xs = new double[this.nodes][];
	}
	
	public StubModelData withWs(double... ws) {
		this.ws = ws;
		return this;
	}
	
	public StubModelData withXs(int node, double... xs) {
		this.xs[node] = xs;
		return this;
	}
}
