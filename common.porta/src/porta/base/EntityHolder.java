package porta.base;

import porta.interfaces.IEntity;


public class EntityHolder<T> implements IEntity {
	
	T instance;
	
	public EntityHolder(T obj) {
		this.instance = obj;
	}
	
	public T get() {
		return instance;
	}
	
}
