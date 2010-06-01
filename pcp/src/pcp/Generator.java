package pcp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import pcp.generator.GeneratorProperties;
import pcp.generator.GraphType;
import pcp.generator.IGraphGenerator;
import pcp.generator.network.RingNetworkGraphGenerator;
import pcp.generator.random.RandomGraphGenerator;
import props.Settings;

public class Generator {
	
	public static void main(String[] args) throws Exception {
		Settings.load("generator");
		
		try {
			GeneratorProperties gp = readProperties();
			IGraphGenerator generator = createGenerator(gp);
			String outdir = Settings.get().getString("generator.outdir");
			String name = Settings.get().getString("generator.name");
			
			if (gp.getGraphsCount() == 1) {
				generateGraph(generator, outdir, name);
			} else {
				for (int i = 0; i < gp.getGraphsCount(); i++) {
					generateGraph(generator, outdir, name + String.format(".%03d", i));
				}
			}
			
			System.out.println("Generation finished successfully" );
			
		} catch (IOException e) {
			System.err.println("Error writing files");
			System.err.println(e.getMessage());
			return;
		}
	}


	private static IGraphGenerator createGenerator(GeneratorProperties gp) {
		switch (gp.getType()) {
			case Random: return new RandomGraphGenerator(gp);
			case Ring: return new RingNetworkGraphGenerator(gp);
		}
		
		return null;
	}


	private static void generateGraph(IGraphGenerator generator, String outdir,
			String name) throws IOException, Exception {
		File file = new File(outdir, name + ".in");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		generator.generate(writer, name);
		writer.close();
		System.out.println("Generation done in " + file.getAbsolutePath());
	}
	
	
	private static GeneratorProperties readProperties() {
		GeneratorProperties gp = new GeneratorProperties();
		gp.setName(Settings.get().getString("generator.name"));
		gp.setEdgeProb(Settings.get().getDouble("generator.edgeprob"));
		gp.setGraphsCount(Settings.get().getInteger("generator.count"));
		gp.setNodeCount(Settings.get().getInteger("generator.nodes"));
		gp.setMinPartition(Settings.get().getInteger("generator.minpartition"));
		gp.setMaxPartition(Settings.get().getInteger("generator.maxpartition"));
		gp.setBase(Settings.get().getInteger("generator.base"));
		gp.setRequests(Settings.get().getDouble("generator.requests"));
		gp.setType(Settings.get().getEnum("generator.graphtype", GraphType.class));
		
		return gp;
	}
	

}
