package pcp.model.strategy;


public enum Adjacency {
	
	AdjacentsLeqColor("ac"),
	AdjacentsLeqOne("a1");

	String shortId;
	
	private Adjacency(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
}
