package pcp.algorithms;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.IModelData;
import pcp.model.Model;


public abstract class Algorithm {
	
	protected IAlgorithmSource provider;
	protected IAlgorithmBounder bounder;
	protected IModelData data;
	protected Model model; 
	
	public Algorithm(IAlgorithmSource provider) {
		this.provider = provider;
		this.bounder = provider.getBounder();
		this.data = provider.getData();
		this.model = provider.getModel();
	}
	
}
