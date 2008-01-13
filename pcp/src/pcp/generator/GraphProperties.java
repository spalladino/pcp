package pcp.generator;

public class GraphProperties implements Cloneable {
	
	protected String name;
	protected double edgeProb;
	protected int nodeCount;
	protected int minPartition;
	protected int maxPartition;
	protected int base;

	public GraphProperties() {
	}
	
	public GraphProperties(String name, double edgeProb, int nodeCount, int minPartition, int maxPartition, int base) {
		this.name = name;
		this.edgeProb = edgeProb;
		this.nodeCount = nodeCount;
		this.minPartition = minPartition;
		this.maxPartition = maxPartition;
		this.base = base;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEdgeProb() {
		return edgeProb;
	}

	public void setEdgeProb(double edgeProb) {
		this.edgeProb = edgeProb;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getMinPartition() {
		return minPartition;
	}

	public void setMinPartition(int minPartition) {
		this.minPartition = minPartition;
	}

	public int getMaxPartition() {
		return maxPartition;
	}

	public void setMaxPartition(int maxPartition) {
		this.maxPartition = maxPartition;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new GraphProperties(name, edgeProb, nodeCount, minPartition, maxPartition, base);
	}
	
}