package pcp.model.strategy;


public enum Symmetry {

	None("sno"),
	UseLowerLabelFirst("slow"),
	VerticesNumber("snum"),
	MinimumNodeLabel("slab");
	
	String shortId;
	
	private Symmetry(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
	
}
