package pcp.model.strategy;


public enum Symmetry {

	None("sno"),
	UseLowerLabelFirst("slow"),
	OnlyUseColorIfNodesPainted("sstrict");
	
	String shortId;
	
	private Symmetry(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
	
}
