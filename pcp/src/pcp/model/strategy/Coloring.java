package pcp.model.strategy;


public enum Coloring {

	Nodes("cn"),
	Partitions("cp"),
	Configuration("cc"),
	DSaturEasyNode("dsn"),
	DSaturHardPartition("dsp");
	
	String shortId;
	
	private Coloring(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
}
