package pcp.interfaces;


public interface IModelData {

	double w(int j);

	double x(int i, int j);
	
	double[] ws();

	double[] xs(int node);

	
}
