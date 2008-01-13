package pcp.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DimacsPartitionedGraph {

	private int minPartition, maxPartition;
	private int nodes;
	private int base;
	
	private double edgeProb;
	private Random random;
	private String name;

	private List<Edge> edgeList;
	private List<Partition> partitionsList;

	static class Edge {
		
		public Integer vertexOne;
		public Integer vertexTwo;
		
		public Edge(Integer vertexOne, Integer vertexTwo) {
			this.vertexOne = vertexOne;
			this.vertexTwo = vertexTwo;
		}
	}
	
	static class Partition {
	
		List<Integer> nodes;
		
		public Partition() {
			this.nodes = new ArrayList<Integer>();
		}
		
		public void addNode(Integer node) {
			if (!nodes.contains(node)) {
				nodes.add(node);
			}
		}
		
		public List<Integer> getNodes() {
			return nodes;
		}

	}

	public DimacsPartitionedGraph(GraphProperties props) {

		// Init fields
		this.nodes = props.nodeCount;
		this.maxPartition = props.maxPartition;
		this.minPartition = props.minPartition;
		this.edgeProb = props.edgeProb;
		this.name = props.name;
		this.base = props.base;
		
		this.random = new Random(System.currentTimeMillis());
		this.edgeList = new ArrayList<Edge>();
		this.partitionsList = new ArrayList<Partition>();
		
		// Check edge probability
		if (edgeProb < 0) edgeProb = 0;
		else if (edgeProb > 1) edgeProb = 1;
		
		// Check partition sizes
		if (minPartition < 1) minPartition = 1;
		if (maxPartition < minPartition) maxPartition = minPartition;
		
		// Create axes
		for (int i = 0; i < nodes; i++)
			for (int j = i+1; j < nodes; j++)
				if (edgeProb > random.nextDouble())
					this.edgeList.add(new Edge(i,j));

		// Create partitions
		int nodeIndex = 0;
		while (nodeIndex < nodes) {
			int currSize = random.nextInt(maxPartition - minPartition + 1) + minPartition;
			Partition partition = new Partition();
			partitionsList.add(partition);
			while (currSize > 0 && nodeIndex < nodes) { 
				partition.addNode(nodeIndex);
				nodeIndex++;
				currSize--;
			}
		}
	}

	public void write(Writer writer) throws IOException {

		writer.write("c nodecnt=" + String.valueOf(nodes) + "\n");
		writer.write("c density=" + String.valueOf(edgeProb) + "\n");

		writer.write("p " + this.name + " " 
				+ String.valueOf(nodes) + " " 
				+ String.valueOf(this.edgeList.size()) + " " 
				+ String.valueOf(this.partitionsList.size()) + "\n");
		
		for (Partition partition : partitionsList) {
			writer.write("q");
			for (Integer node : partition.getNodes()) {
				writer.write(" " + String.valueOf(node+base));
			} writer.write("\n");
		}
		
		for (Edge axis : this.edgeList) {
			writer.write("e " + String.valueOf(axis.vertexOne+base) + " " + String.valueOf(axis.vertexTwo+base) + "\n");
		}
		
		writer.write("\n");
	}

	
}
