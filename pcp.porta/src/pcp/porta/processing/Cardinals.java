package pcp.porta.processing;

import pcp.Settings;

public class Cardinals {
	
	public Integer colorCount;
	public Integer nodeCount;
	public Boolean projectColors;
	public Integer nodeVarsCount;
	
	public Cardinals(Integer nodes, Integer colors) {
		this.colorCount = colors;
		this.nodeCount = nodes;
		
		this.projectColors = Settings.get().getBoolean("porta.projectColors");
		this.nodeVarsCount = colors * nodes;
	}
	
	public Cardinals(Integer nodes, Integer colors, boolean project) {
		this.colorCount = colors;
		this.nodeCount = nodes;
		this.projectColors = project;
		this.nodeVarsCount = colors * nodes;
	}
	
	public static Cardinals fromPortaSettings() {
		int nodeCount = Settings.get().getInteger("porta.nodeCount");
		int colorCount = Settings.get().getInteger("porta.colorCount");
		return new Cardinals(nodeCount, colorCount);
	}
	
}
