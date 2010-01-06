package pcp.model.strategy;


public enum Partition {
	
	PaintExactlyOne("pexact"),
	PaintAtLeastOne("pgeq");
	
	String shortId;
	
	private Partition(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
}
