package pcp.generator;

import java.io.Writer;

public class SimpleGraphGenerator implements IGraphGenerator {
	
	GeneratorProperties properties;
	
	public SimpleGraphGenerator(GeneratorProperties properties) {
		super();
		this.properties = properties;
	}

	
	/* (non-Javadoc)
	 * @see pcp.generator.IGraphGenerator#Generate(java.io.Writer)
	 */
	public void generate(Writer writer, String name) throws Exception {
		GraphProperties gp = (GraphProperties) properties.clone();
		gp.setName(name);
		DimacsPartitionedGraph graph = new DimacsPartitionedGraph(gp);
		graph.write(writer);
	}
	
	
	
}
