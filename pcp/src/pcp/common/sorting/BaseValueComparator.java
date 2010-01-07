package pcp.common.sorting;

import java.util.Comparator;

import pcp.interfaces.IModelData;

public abstract class BaseValueComparator<T> implements Comparator<T> {

	protected IModelData data;
	
	public BaseValueComparator(IModelData data) {
		super();
		this.data = data;
	}
	
	
	
}
