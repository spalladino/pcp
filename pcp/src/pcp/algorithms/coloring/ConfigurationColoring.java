package pcp.algorithms.coloring;

import pcp.entities.IPartitionedGraph;
import props.Settings;


public class ConfigurationColoring extends NodesColoring {

	Integer colors;
	
	public ConfigurationColoring(IPartitionedGraph graph) {
		super(graph);
		colors = Settings.get().getInteger("coloring.colors");
	}
	
	@Override
	public Integer getChi() {
		if (colors != null) return colors;
		else return super.getChi();
	}
	
}
