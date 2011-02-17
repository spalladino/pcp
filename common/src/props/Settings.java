package props;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class Settings {

	static Settings instance;
	private final static boolean safe = true;
	
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
	
	Properties systemProps;
	Properties runProps;
	
	private Settings(String name) throws Exception {
		props = new Properties();
		runProps = new Properties();
		systemProps = System.getProperties();
		
		runProps.load(new FileInputStream(new File(name + ".properties")));
		
		props.putAll(runProps);
		props.putAll(systemProps);
	}
	
	public <T extends Enum<T>> T getEnum(String prop, Class<T> clazz) {
		return Enum.valueOf(clazz, getProperty(prop));
	}
	
	public boolean hasSetting(String name) {
		return props.containsKey(name);
	}
	
	public String getPath(String folder, String filename) {
		return getProperty(folder) + getProperty(filename);
	}
	
	public String getString(String name) {
		return getProperty(name);
	}
	
	public Boolean getBoolean(String name) {
		try { return Boolean.valueOf(getProperty(name)); }
		catch (Exception ex) { if (safe) throw new RuntimeException("Invalid setting " + name); else return null; }
	}
	
	public Double getDouble(String name) {
		try { return Double.valueOf(getProperty(name)); }
		catch (Exception ex)  { if (safe) throw new RuntimeException("Invalid setting " + name); else return null; }
	}

	public Long getLong(String name) {
		try { return Long.valueOf(getProperty(name)); }
		catch (Exception ex)  { if (safe) throw new RuntimeException("Invalid setting " + name); else return null; }
	}
	
	public Integer getInteger(String name) {
		try { return Integer.valueOf(getProperty(name)); }
		catch (Exception ex)  { if (safe) throw new RuntimeException("Invalid setting " + name); else return null; }
	}
	
	public Properties getProps() {
		return props;
	}
	
	@Override
	public String toString() {
		return props == null ? "Settings" : props.toString();
	}
	
	private String getProperty(String name) {
		String property = props.getProperty(name);
		if (safe && property == null) throw new RuntimeException("Invalid setting " + name);
		else return property;
	}

	
	public Properties getSystemProps() {
		return systemProps;
	}

	
	public Properties getRunProps() {
		return runProps;
	}
	
}
