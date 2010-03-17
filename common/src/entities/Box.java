package entities;

public class Box<T> {
	
	T data;

	public Box(T data) {
		this.data = data;
	}


	public T getData() {
		return data;
	}

	
	public void setData(T data) {
		this.data = data;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Box other = (Box) obj;
		if (data == null) {
			if (other.data != null) return false;
		} else if (!data.equals(other.data)) return false;
		return true;
	}
	
}
