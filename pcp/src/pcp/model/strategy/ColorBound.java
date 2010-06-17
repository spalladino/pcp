package pcp.model.strategy;

public enum ColorBound {

	None("cbn"),
	UpperNodesSum("cbuxs"),
	UpperNodesSumLowerSum("cbb"),
	UpperNodesSumLowerSumPartition("cbbp");
	
	String shortId;
	
	private ColorBound(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
	
}
