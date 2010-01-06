package pcp.generator;

public class GeneratorProperties extends GraphProperties {
	
	int graphsCount;
	
	public GeneratorProperties(String name, double edgeProb, int graphsCount, int nodeCount, int minPartition,
			int maxPartition) {
		super();
		this.name = name;
		this.edgeProb = edgeProb;
		this.graphsCount = graphsCount;
		this.nodeCount = nodeCount;
		this.minPartition = minPartition;
		this.maxPartition = maxPartition;
	}
	
	public GeneratorProperties() {
		super();
	}
	
	public int getGraphsCount() {
		return graphsCount;
	}
	
	public void setGraphsCount(int graphsCount) {
		this.graphsCount = graphsCount;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new GeneratorProperties(name, edgeProb, graphsCount, nodeCount, minPartition, maxPartition);
	}
}
