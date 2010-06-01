package pcp.generator;

public class GeneratorProperties extends GraphProperties {
	
	int graphsCount;
	
	public GeneratorProperties(String name, double edgeProb, int graphsCount, int nodeCount, int minPartition,
			int maxPartition, int base, double requests, GraphType type) {
		super();
		this.name = name;
		this.edgeProb = edgeProb;
		this.graphsCount = graphsCount;
		this.nodeCount = nodeCount;
		this.minPartition = minPartition;
		this.maxPartition = maxPartition;
		this.base = base;
		this.requests = requests;
		this.type = type;
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
	public Object clone() throws CloneNotSupportedException {
		return new GeneratorProperties(name, edgeProb, graphsCount, nodeCount, minPartition, maxPartition, base, requests, type);
	}
}
