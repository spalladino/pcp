package pcp.entities.partitioned;

import java.util.Arrays;
import java.util.Comparator;

import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;

public class SortedPartitionedGraph implements IPartitionedGraph {

	PartitionedGraph graph;
	ISimpleGraph gprime;
	
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
		return getPartitionNodes(partition.name);
	}

	private Node[] getPartitionNodes(int index) {
		if (this.partitionNodes[index] == null) {
			this.partitionNodes[index] = graph.partitionNodes[index].clone();
			Arrays.sort(this.partitionNodes[index], this.nodeComparer);
		}
		return this.partitionNodes[index];
	}

	@Override
	public Node[] getNodes(pcp.entities.simple.Node simpleNode) {
		return getPartitionNodes(simpleNode.index());
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
			final Node[] p = getNodes(getPartition(node));
			final Node[] target = new Node[n.length + p.length - 1];
			
			int i = 0;
			int j = 0;
			int t = 0;			
			
			while (i < n.length || j < p.length) {
				if ((j >= p.length) || (i < n.length && nodeComparer.compare(n[i], p[j]) <= 0 )) {
					if (n[i].index() != node.index()) {
						target[t] = n[i]; t++;
					} i++;
				} else {
					if (p[j].index() != node.index()) {
						target[t] = p[j]; t++;
					} j++;
				} 
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
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public boolean areInSamePartition(Node n1, Node n2) {
		return graph.areInSamePartition(n1, n2);
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

	@Override
	public Partition[] getNeighbourPartitions(Node n) {
		throw new UnsupportedOperationException();
		//return graph.getNeighbourPartitions(n);
	}

	@Override
	public Partition[] getNeighbourPartitions(Partition p) {
		throw new UnsupportedOperationException();
		//return graph.getNeighbourPartitions(p);
	}

	@Override
	public Node[] getNeighbours(Partition p) {
		throw new UnsupportedOperationException();
		//return graph.getNeighbours(p);
	}

	@Override
	public ISimpleGraph getGPrime() {
		if (gprime == null) {
			gprime = new GPrimeBuilder(this).getGraph();
		} return gprime;
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public Partition getPartition(int partition) {
		return graph.getPartition(partition);
	}
	
}
