package pcp;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import props.Settings;


public class ExecutionData {
	
	Map<String, Object> data;
	
	public ExecutionData() {
		this.data = new HashMap<String, Object>();
	}
	
	@SuppressWarnings("unchecked")
	public ExecutionData withSettings() {
		this.data.putAll((Map)(Settings.get().getProps()));
		return this;
	}
	
	public Map<String, Object> getData() {
		return data;
	}

	public void dump() {
		try {
			String dumpfile = Settings.get().getPath("data.folder", "data.filename");
			FileWriter writer = new FileWriter(dumpfile, true);
			DumperOptions opts = new DumperOptions();
			Yaml yaml = new Yaml(opts);
			yaml.dump(data, writer);
			writer.close();
		} catch (Exception ex) {
			System.err.println("Error dumping data: " + ex.getMessage());
		}
	}
	
}
