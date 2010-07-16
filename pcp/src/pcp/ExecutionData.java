package pcp;

import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import pcp.entities.IPartitionedGraph;
import props.Settings;


public class ExecutionData {
	
	Map<String, Object> data;
	
	public ExecutionData() {
		this.data = new TreeMap<String, Object>();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExecutionData withSettings() {
		this.data.putAll((Map)(Settings.get().getProps()));
		return this;
	}
	
	public ExecutionData withProblemSettings() {
		for (Object prop : Settings.get().getProps().keySet()) {
			if (Settings.get().getRunProps().containsKey(prop)) {
				this.data.put(prop.toString(), Settings.get().getProps().get(prop));
			}
		}
		return this;
	}
	
	public Map<String, Object> getData() {
		return data;
	}

	public void dump() {
		try {
			String dumpfile = Settings.get().getPath("data.folder", "data.filename");
			FileWriter writer = new FileWriter(dumpfile, false);
			DumperOptions opts = new DumperOptions();
			Yaml yaml = new Yaml(opts);
			yaml.dump(data, writer);
			writer.close();
		} catch (Exception ex) {
			System.err.println("Error dumping data: " + ex.getMessage());
		}
	}

	public ExecutionData withOriginalInputData(IPartitionedGraph graph) {
		return withGraphData(graph, "original.graph");
	}

	public ExecutionData withInputData(IPartitionedGraph graph) {
		return withGraphData(graph, "graph");
	}
	
	private ExecutionData withGraphData(IPartitionedGraph graph, String prefix) {
		this.data.put(prefix + ".nodes", graph.N());
		this.data.put(prefix + ".edges", graph.E());
		this.data.put(prefix + ".partitions", graph.P());
		
		this.data.put(prefix + ".partitionsize", (float)graph.N() / (float)graph.P());
		this.data.put(prefix + ".density", (float)(2*graph.E()) / (float)(graph.N() * (graph.N()-1)));
		
		return this;
	}
	
}
