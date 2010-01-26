package pcp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class Settings {

	static Settings instance;
		
	public static void load(String name) throws Exception {
		instance = new Settings(name);
	}
	
	public static void init() {
		try {instance = new Settings(null); } 
		catch (Exception e) { }
	}
	
	public static Settings get() {
		return instance;
	}
	
	Properties props;
	
	private Settings(String name) throws Exception {
		props = new Properties();
		if (name != null) props.load(new FileInputStream(new File(name + ".properties")));
	}
	
	public <T extends Enum<T>> T getEnum(String prop, Class<T> clazz) {
		return Enum.valueOf(clazz, getString(prop));
	}
	
	public String getPath(String filename, String folder) {
		return getString(folder) + getString(filename);
	}
	
	public String getString(String name) {
		return props.getProperty(name);
	}
	
	public Boolean getBoolean(String name) {
		try { return Boolean.valueOf(props.getProperty(name)); }
		catch (Exception ex) { return null; }
	}
	
	public Double getDouble(String name) {
		try { return Double.valueOf(props.getProperty(name)); }
		catch (Exception ex) { return null; }
	}
	
	public Integer getInteger(String name) {
		try { return Integer.valueOf(props.getProperty(name)); }
		catch (Exception ex) { return null; }
	}
	
}
