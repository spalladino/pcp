package pcp.entities;

import java.util.Arrays;
import java.util.Comparator;

import pcp.interfaces.IPartitionedGraph;

public class SortedPartitionedGraph implements IPartitionedGraph {

	PartitionedGraph graph;
	
	Comparator<Partition> partitionComparer;
	Comparator<Node> nodeComparer;
	Comparator<Edge> edgeComparer;
	
	Node[][] adjacencies;
	Node[][] partitionNodes;
	Node[][] adjacenciesCopartitionNodes;
	
	Node[] nodes;
	Partition[] partitions;
	Edge[] edges;
	
	public SortedPartitionedGraph(PartitionedGraph graph,
			Comparator<Node> nodeComparer,
			Comparator<Edge> edgeComparer,
			Comparator<Partition> partitionComparer) {
		
		super();
		
		this.graph = graph;
		
		this.edgeComparer = edgeComparer;
		this.nodeComparer = nodeComparer;
		this.partitionComparer = partitionComparer;
		
		reset();
	}

	public void reset() {
		if (this.edgeComparer != null) {
			this.edges = graph.edges.clone();
			Arrays.sort(this.edges, this.edgeComparer);
		} else {
			this.edges = graph.edges;
		}
		
		if (this.nodeComparer != null) {
			this.nodes = graph.nodes.clone();
			Arrays.sort(this.nodes, this.nodeComparer);
		} else {
			this.nodes = graph.nodes;
		}
		
		if (this.partitionComparer != null) {
			this.partitions = graph.partitions.clone();
			Arrays.sort(this.partitions, this.partitionComparer);
		}  else {
			this.partitions = graph.partitions;
		}
		
		if (this.nodeComparer != null) {
			this.adjacencies = new Node[graph.N()][];
			this.partitionNodes = new Node[graph.P()][];
		} else {
			this.adjacencies = graph.adjacencies;
			this.partitionNodes = graph.partitionNodes;
		} 
		
		this.adjacenciesCopartitionNodes = new Node[graph.N()][];
	}
	
	@Override
	public Node[] getNodes() {
		return this.nodes;
	}

	@Override
	public Partition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public Edge[] getEdges() {
		return this.edges;
	}

	@Override
	public Node[] getNodes(Partition partition) {
		if (this.partitionNodes[partition.name] == null) {
			this.partitionNodes[partition.name] = graph.partitionNodes[partition.name].clone();
			Arrays.sort(this.partitionNodes[partition.name], this.nodeComparer);
		}
		return this.partitionNodes[partition.name];
	}

	@Override
	public Node[] getNeighbours(Node node) {
		if (this.adjacencies[node.name] == null) {
			this.adjacencies[node.name] = graph.adjacencies[node.name].clone();
			Arrays.sort(this.adjacencies[node.name], this.nodeComparer);
		}
		return this.adjacencies[node.name];
	}

	public Node[] getNeighboursPlusCopartition(Node node) {
		if (this.adjacenciesCopartitionNodes[node.name] == null) {
			final Node[] n = getNeighbours(node);
			final Node[] p = getPartition(node).getNodes();
			final Node[] target = new Node[n.length + p.length - 1];
			
			int i = 0;
			int j = 0;
			int t = 0;			
			
			while (i < n.length || j < p.length) {
				if (j >= p.length || nodeComparer.compare(n[i], p[j]) <= 0 ) {
					if (n[i].index() != node.index()) {
						target[t] = n[i]; 
					} i++;
				} else {
					if (p[j].index() != node.index()) {
						target[t] = p[j];
					} j++;
				} t++;
			}
			
			this.adjacenciesCopartitionNodes[node.name] = target;
		}
		
		return this.adjacenciesCopartitionNodes[node.name];
	}
	
	@Override
	public Node getNode(int index) {
		return graph.getNode(index);
	}

	@Override
	public int getDegree(Node node) {
		return graph.getDegree(node);
	}

	@Override
	public Partition getPartition(Node node) {
		return graph.getPartition(node);
	}

	@Override
	public boolean areAdjacent(Node n1, Node n2) {
		return areAdjacent(n1, n2);
	}

	@Override
	public int N() {
		return graph.N();
	}

	@Override
	public int E() {
		return graph.E();
	}

	@Override
	public int P() {
		return graph.P();
	}

	@Override
	public String getName() {
		return graph.getName();
	}
	
	
	
}
