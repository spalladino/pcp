package pcp.common.iterate;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

	private T[] array;
	private int index;
	
	public ArrayIterator(T[] array) {
		this.array = array;
		this.index = -1;
	}
	
	@Override
	public boolean hasNext() {
		return this.index < array.length - 1;
	}

	@Override
	public T next() {
		return array[++index];
	}

	@Override
	public void remove() {
		array[index] = null;
	}
	
	public T current() {
		return array[index];
	}
	
	public T currentOrDefault() {
		if (index < 0 || index >= array.length)
			return null;
		return array[index];
	}

	
}
