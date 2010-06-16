package pcp.algorithms;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.bounding.TimeBounder;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.IModelData;
import pcp.model.Model;


public abstract class Algorithm implements IBoundedAlgorithm {
	
	protected IAlgorithmSource provider;
	protected IAlgorithmBounder bounder;
	protected IModelData data;
	protected Model model; 
	
	public Algorithm(IAlgorithmSource provider) {
		this.provider = provider;
		this.bounder = new TimeBounder();
		this.data = provider.getData();
		this.model = provider.getModel();
	}
	
	@Override
	public IAlgorithmBounder getBounder() {
		return this.bounder;
	}
	
}
