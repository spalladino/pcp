package pcp.porta;

import porta.BaseParameters;
import props.Settings;

public class PcpCardinals extends BaseParameters {
	
	public Integer colorCount;
	public Integer nodeCount;
	public Boolean projectColors;
	public Integer nodeVarsCount;
	
	public PcpCardinals () {
		this(Settings.get().getInteger("porta.nodeCount"),
			Settings.get().getInteger("porta.colorCount"),
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public PcpCardinals(Integer nodes) {
		this(nodes,
			Settings.get().getInteger("porta.colorCount"),
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public PcpCardinals(Integer nodes, Integer colors) {
		this(nodes,
			colors,
			Settings.get().getBoolean("porta.projectColors"));
	}
	
	public PcpCardinals(Integer nodes, Integer colors, boolean project) {
		this.colorCount = colors;
		this.nodeCount = nodes;
		this.projectColors = project;
		this.nodeVarsCount = colors * nodes;
	}
	
	
	
}
