package pcp.interfaces;

import pcp.model.Model;


public interface IAlgorithmSource {
	
	ICutBuilder getCutBuilder();
	
	IModelData getData();
	
	ISortedProvider getSorted();
	
	Model getModel();
	
}
