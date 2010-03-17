package pcp.porta;

import porta.base.BaseParameters;
import props.Settings;

public class Parameters extends BaseParameters {
	
	public Integer colorCount;
	public Integer nodeCount;
	public Boolean projectColors;
	public Integer nodeVarsCount;
	
	public Parameters () {
		this(Settings.get().getInteger("porta.nodeCount"),
			Settings.get().getInteger("porta.colorCount"),
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public Parameters(Integer nodes) {
		this(nodes,
			Settings.get().getInteger("porta.colorCount"),
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public Parameters(Integer nodes, Integer colors) {
		this(nodes,
			colors,
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public Parameters(Integer nodes, Integer colors, boolean project) {
		this.colorCount = colors;
		this.nodeCount = nodes;
		this.projectColors = project;
		this.nodeVarsCount = colors * nodes;
	}
	
	
	
}
