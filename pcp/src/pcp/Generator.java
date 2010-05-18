package pcp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import pcp.generator.GeneratorProperties;
import pcp.generator.IGraphGenerator;
import pcp.generator.SimpleGraphGenerator;

public class Generator {
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream(new File("generator.properties")));
		} catch(IOException e) {
			System.err.println("Error opening generator.properties");
			System.err.println(e.getMessage());
			return;
		} 
		
		try {
			GeneratorProperties gp = readProperties(props);
			IGraphGenerator generator = createGenerator(gp);
			for (int i = 0; i < gp.getGraphsCount(); i++) {
				String name = props.getProperty("generator.name") + String.format(".%03d", i);
				File file = new File(props.getProperty("generator.outdir"), name + ".in");
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				generator.generate(writer, name);
				writer.close();
				System.out.println("Generation done in " + file.getAbsolutePath());
			}
			
			System.out.println("Generation finished successfully" );
			
		} catch (IOException e) {
			System.err.println("Error writing files");
			System.err.println(e.getMessage());
			return;
		}
	}
	
	
	private static GeneratorProperties readProperties(Properties props) {
		GeneratorProperties gp = new GeneratorProperties();
		gp.setName(props.getProperty("generator.name"));
		gp.setEdgeProb(getDouble(props, "generator.edgeprob"));
		gp.setGraphsCount(getInteger(props, "generator.count"));
		gp.setNodeCount(getInteger(props, "generator.nodes"));
		gp.setMinPartition(getInteger(props, "generator.minpartition"));
		gp.setMaxPartition(getInteger(props, "generator.maxpartition"));
		gp.setBase(getInteger(props, "generator.base"));
		return gp;
	}

	private static int getInteger(Properties props, String key) {
		return Integer.valueOf(props.getProperty(key));
	}
	
	private static double getDouble(Properties props, String key) {
		return Double.valueOf(props.getProperty(key));
	}

	private static IGraphGenerator createGenerator(GeneratorProperties gp) {
		return new SimpleGraphGenerator(gp);
	}
	
}
