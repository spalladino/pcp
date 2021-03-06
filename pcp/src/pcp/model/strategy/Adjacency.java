package pcp.model.strategy;


public enum Adjacency {
	
	AdjacentsLeqColor("ac"),
	AdjacentsLeqOne("a1"),
	AdjacentsNeighbourhood("an"),
	AdjacentsPartitionLeqColor("ap");

	String shortId;
	
	private Adjacency(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
}
