package pcp.generator.network;

import java.io.Writer;
import java.util.List;

import pcp.generator.DimacsPartitionedGraph;
import pcp.generator.GeneratorProperties;
import pcp.generator.IGraphGenerator;

public abstract class NetworkGraphGenerator implements IGraphGenerator {

	protected GeneratorProperties gp;

	public NetworkGraphGenerator(GeneratorProperties gp) {
		this.gp = gp;
	}
	
	protected abstract List<Lightpath> getPaths();

	@Override
	public void generate(Writer writer, String name) throws Exception {
		DimacsPartitionedGraph graph = new NetworkGraphBuilder(gp).buildGraph(getPaths());
		graph.setName(name);
		graph.write(writer);
	}



}
