package pcp.algorithms.bounding;


public interface IBoundedAlgorithm {

	IAlgorithmBounder getBounder();
	
	Integer getIdentifier();
	
}
