package pcp.solver.data;

import pcp.common.sorting.SortedProvider;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.ICutBuilder;
import pcp.interfaces.IModelData;
import pcp.interfaces.ISortedProvider;
import pcp.model.Model;


public class Iteration implements IAlgorithmSource, Cloneable {
	
	IModelData data;
	ICutBuilder cutBuilder;
	Model model;
	SortedProvider sorted;
	
	
	public Iteration(Model model, IModelData data, ICutBuilder cutBuilder) {
		this.model = model;
		this.data = data;
		this.cutBuilder = cutBuilder;
		this.sorted = new SortedProvider(model, data);
	}
	
	@Override
	public Iteration clone() {
		return this;
		//return new Iteration(model, data, cutBuilder);
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
	public IModelData getData() {
		return data;
	}

	@Override
	public ISortedProvider getSorted() {
		return sorted;
	}
	
}
