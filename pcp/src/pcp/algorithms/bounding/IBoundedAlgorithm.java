package pcp.algorithms.bounding;

import pcp.solver.cuts.CutFamily;


public interface IBoundedAlgorithm {

	IAlgorithmBounder getBounder();
	
	CutFamily getIdentifier();
	
}
