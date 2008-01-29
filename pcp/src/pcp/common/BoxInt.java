package pcp.common;


public class BoxInt extends Box<Integer> {

	public BoxInt(Integer data) {
		super(data);
	}
	
	public Integer incData() {
		return ++this.data;
	}
	
}
