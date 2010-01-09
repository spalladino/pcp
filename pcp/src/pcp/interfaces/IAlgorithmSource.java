package pcp.interfaces;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.model.Model;


public interface IAlgorithmSource {

	IAlgorithmBounder getBounder();
	
	ICutBuilder getCutBuilder();
	
	IModelData getData();
	
	ISortedProvider getSorted();
	
	Model getModel();
	
}
