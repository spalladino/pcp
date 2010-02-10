package pcp.solver.data;

import pcp.algorithms.bounding.Bounder;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.common.sorting.SortedProvider;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.interfaces.ISortedProvider;
import pcp.model.Model;


public class Iteration implements IAlgorithmSource {
	
	Integer currentAlgorithm;
	
	IModelData data;
	ICutBuilder cutBuilder;
	
	Model model;
	Bounder bounder;
	SortedProvider sorted;
	
	
	public Iteration(Model model, IModelData data, ICutBuilder cutBuilder) {
		this(model, data, cutBuilder, null);
	}
	
	private Iteration(Model model, IModelData data, ICutBuilder cutBuilder, Integer algorithm) {
		this.model = model;
		this.data = data;
		this.cutBuilder = cutBuilder;
		this.sorted = new SortedProvider(model, data);
		this.bounder = new Bounder();
		this.currentAlgorithm = algorithm;
	}
	
	public Iteration forAlgorithm() {
		return forAlgorithm(null);
	}
	
	public Iteration forAlgorithm(Integer code) {
		return new Iteration(model, data, cutBuilder, code);
	}

	@Override
	public ICutBuilder getCutBuilder() {
		return cutBuilder;
	}

	@Override
	public Model getModel() {
		return model;
	}


	@Override
	public IAlgorithmBounder getBounder() {
		return bounder;
	}


	@Override
	public IModelData getData() {
		return data;
	}


	@Override
	public ISortedProvider getSorted() {
		return sorted;
	}
	
}
