package pcp.entities.simple;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.entities.ISimpleGraph;


public class InducedSimpleGraph implements ISimpleGraph {
	
	ISimpleGraph graph;

	Map<Integer, Node> nodesMap;
	Map<Integer, Node[]> nodesAdjacencies;

	public InducedSimpleGraph(ISimpleGraph graph, Node[] nodes) {
		this.graph = graph;
		
		this.nodesAdjacencies = new HashMap<Integer, Node[]>(nodes.length);
		this.nodesMap = new HashMap<Integer, Node>(nodes.length);
		for (Node node : nodes) {
			this.nodesMap.put(node.name, new Node(this, node.name));
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
	public Node[] getNeighbours(Node simpleNode) {
		if (!this.nodesAdjacencies.containsKey(simpleNode.index())) {
			this.nodesAdjacencies.put(simpleNode.name, calculateNeighbours(simpleNode));
		} return this.nodesAdjacencies.get(simpleNode.index());
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
		Collection<Node> values = nodesMap.values();
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
	
	public void print(PrintStream stream) {
		stream.println("|N|= " + N());
		for (Node n : getNodes()) {
			stream.println(n.toString() + ": " + Arrays.toString(getNeighbours(n)));
		}
	}
	
	
}
