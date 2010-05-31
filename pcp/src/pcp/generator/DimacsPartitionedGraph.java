package pcp.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class DimacsPartitionedGraph {

	protected int nodes;
	protected int base;
	protected String name;
	protected List<Edge> edgeList;
	protected List<Partition> partitionsList;

	public static class Edge {
			
		public Integer vertexOne;
		public Integer vertexTwo;
		
		public Edge(Integer vertexOne, Integer vertexTwo) {
			this.vertexOne = vertexOne;
			this.vertexTwo = vertexTwo;
		}
	}

	public static class Partition {
		
		List<Integer> nodes;
		
		public Partition() {
			this.nodes = new ArrayList<Integer>();
		}
		
		public Partition(List<Integer> nodes) {
			this.nodes = nodes;
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
		super();
		this.nodes = props.getNodeCount();
		this.name = props.getName();
		this.base = props.getBase();
		
		this.edgeList = new ArrayList<Edge>();
		this.partitionsList = new ArrayList<Partition>();
	}

	public void addPartition(List<Integer> nodes) {
		List<Integer> copy = new ArrayList<Integer>(nodes.size());
		copy.addAll(nodes);
		this.partitionsList.add(new Partition(copy));
	}
	
	public void addEdge(int n1, int n2) {
		if (n1 < n2) this.edgeList.add(new Edge(n1, n2));
		else this.edgeList.add(new Edge(n2, n1));
	}
	
	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public String getName() {
		return name;
	}

	public List<Edge> getEdgeList() {
		return edgeList;
	}

	public List<Partition> getPartitionsList() {
		return partitionsList;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void write(Writer writer) throws IOException {
	
		writer.write("c nodecnt=" + String.valueOf(nodes) + "\n");
		
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