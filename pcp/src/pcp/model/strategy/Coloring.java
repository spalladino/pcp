package pcp.model.strategy;

import java.util.Arrays;
import java.util.List;


public enum Coloring {

	Nodes("cn"),
	Partitions("cp"),
	Configuration("cc"),
	DSaturEasyNode("dsn"),
	DSaturHardPartition("dsp"), 
	DSaturEasyNodeRandomized("dsnr");
	
	String shortId;
	
	public static final List<Coloring> supportingAssignment = Arrays.asList(DSaturEasyNode, DSaturHardPartition);
	
	private Coloring(String shortId) {
		this.shortId = shortId;
	}
	
	public String getShortId() {
		return this.shortId;
	}
}
