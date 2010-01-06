package pcp.common.iterate;


public abstract class IntegerPairEnumerate {
		
	protected int m;
	protected int n;

	public IntegerPairEnumerate(int n, int m) {
		this.n = n;
		this.m = m;
	}
	
	public void run() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				item(i, j);
			}
		}
	}
	
	protected abstract void item(int i, int j);
	
}
