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
	public void Generate(Writer writer) throws Exception {
		for (int i = 0; i < properties.graphsCount; i++) {
			GraphProperties gp = (GraphProperties) properties.clone();
			gp.setName(properties.name + String.valueOf(i));
			DimacsPartitionedGraph graph = new DimacsPartitionedGraph(gp);
			graph.write(writer);
		}
	}
	
	
	
}
