package pcp.generator.random;

import java.io.Writer;

import pcp.generator.DimacsPartitionedGraph;
import pcp.generator.GeneratorProperties;
import pcp.generator.GraphProperties;
import pcp.generator.IGraphGenerator;

public class RandomGraphGenerator implements IGraphGenerator {
	
	GeneratorProperties properties;
	
	public RandomGraphGenerator(GeneratorProperties properties) {
		super();
		this.properties = properties;
	}

	
	/* (non-Javadoc)
	 * @see pcp.generator.IGraphGenerator#Generate(java.io.Writer)
	 */
	public void generate(Writer writer, String name) throws Exception {
		GraphProperties gp = (GraphProperties) properties.clone();
		gp.setName(name);
		DimacsPartitionedGraph graph = new DimacsRandomPartitionedGraph(gp);
		graph.write(writer);
	}
	
	
	
}
