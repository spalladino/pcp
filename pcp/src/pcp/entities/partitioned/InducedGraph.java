package pcp.entities.partitioned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;


public class InducedGraph implements IPartitionedGraph {
	
	IPartitionedGraph graph;
	ISimpleGraph gprime;
	
	Map<Integer, Node> nodesMap;
	Map<Integer, Node[]> nodesAdjacencies;

	Map<Integer, Partition> partitionsMap;
	Map<Integer, Node[]> partitionsNodes;
	
	public InducedGraph(IPartitionedGraph graph, Node[] nodes) {
		this.graph = graph;
		
		this.nodesAdjacencies = new HashMap<Integer, Node[]>(nodes.length);
		this.nodesMap = new HashMap<Integer, Node>(nodes.length);
		
		this.partitionsNodes = new HashMap<Integer, Node[]>(nodes.length/2);
		this.partitionsMap= new HashMap<Integer, Partition>(nodes.length/2);
		
		for (Node node : nodes) {
			this.nodesMap.put(node.name, new Node(this, node.name));
			int index = node.getPartition().index();
			if (!this.partitionsMap.containsKey(index)) {
				this.partitionsMap.put(index, new Partition(this, index));
			} 
		}
		
		
		
	}

	@Override
	public boolean areAdjacent(Node n1, Node n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public int getDegree(Node node) {
		return this.getNeighbours(node).length;
	}

	@Override
	public Edge[] getEdges() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node[] getNeighbours(Node node) {
		if (!this.nodesAdjacencies.containsKey(node.index())) {
			this.nodesAdjacencies.put(node.name, calculateNeighbours(node));
		} return this.nodesAdjacencies.get(node.index());
	}
	
	private Node[] calculateNeighbours(Node node) {
		List<Node> neighbours = new ArrayList<Node>();
		for (Node n : graph.getNeighbours(node)) {
			if (nodesMap.containsKey(n.name)) {
				neighbours.add(nodesMap.get(n.name));
			}
		} return (Node[]) neighbours.toArray(new Node[neighbours.size()]);
	}
	
	@Override
	public Node getNode(int index) {
		return nodesMap.get(index);
	}

	@Override
	public Node[] getNodes() {
		Collection<Node> values = this.nodesMap.values();
		return (Node[]) values.toArray(new Node[values.size()]);
	}

	@Override
	public int E() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int N() {
		return nodesMap.size();
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public String getName() {
		return graph.getName();
	}

	@Override
	public int P() {
		return this.partitionsMap.size();
	}

	@Override
	public boolean areInSamePartition(Node n1, Node n2) {
		return graph.areInSamePartition(n1, n2);
	}

	@Override
	public ISimpleGraph getGPrime() {
		if (gprime == null) {
			gprime = new GPrimeBuilder(this).getGraph();
		} return gprime;
	}

	@Override
	public Partition[] getNeighbourPartitions(Node n) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Partition[] getNeighbourPartitions(Partition p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node[] getNeighbours(Partition p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node[] getNodes(Partition partition) {
		if (!this.nodesAdjacencies.containsKey(partition.index())) {
			this.nodesAdjacencies.put(partition.index(), calculatePartition(partition));
		} return this.nodesAdjacencies.get(partition.index());
	}

	private Node[] calculatePartition(Partition partition) {
		List<Node> nodes = new ArrayList<Node>();
		for (Node n : graph.getNodes(partition)) {
			if (nodesMap.containsKey(n.name)) {
				nodes.add(nodesMap.get(n.name));
			}
		} return (Node[]) nodes.toArray(new Node[nodes.size()]);
	}

	@Override
	public Partition getPartition(Node node) {
		return this.partitionsMap.get(node.getPartition().index());
	}

	@Override
	public Partition[] getPartitions() {
		Collection<Partition> values = this.partitionsMap.values();
		return (Partition[]) values.toArray(new Partition[values.size()]);
	}
	
	
	
	
}
