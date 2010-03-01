package pcp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pcp.interfaces.ISimpleGraph;


public class InducedSimpleGraph implements ISimpleGraph {
	
	ISimpleGraph graph;
	SimpleNode[] nodes;
	Map<Integer, SimpleNode> nodesMap;
	Map<Integer, SimpleNode[]> nodesAdjacencies;

	public InducedSimpleGraph(ISimpleGraph graph, SimpleNode[] nodes) {
		this.graph = graph;
		this.nodes = nodes;
	}

	@Override
	public boolean areAdjacent(SimpleNode n1, SimpleNode n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public int getDegree(SimpleNode node) {
		return this.getNeighbours(node).length;
	}

	@Override
	public SimpleEdge[] getEdges() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SimpleNode[] getNeighbours(SimpleNode simpleNode) {
		if (!this.nodesAdjacencies.containsKey(simpleNode.index())) {
			this.nodesAdjacencies.put(simpleNode.name, calculateNeighbours(simpleNode));
		} return this.nodesAdjacencies.get(simpleNode.index());
	}
	
	private SimpleNode[] calculateNeighbours(SimpleNode node) {
		List<SimpleNode> neighbours = new ArrayList<SimpleNode>();
		for (SimpleNode n : graph.getNeighbours(node)) {
			if (nodesMap.containsKey(n.name)) {
				neighbours.add(nodesMap.get(n.name));
			}
		} return (SimpleNode[]) neighbours.toArray(new SimpleNode[neighbours.size()]);
	}
	
	@Override
	public SimpleNode getNode(int index) {
		return nodesMap.get(index);
	}

	@Override
	public SimpleNode[] getNodes() {
		return nodes;
	}

	@Override
	public int E() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int N() {
		return nodes.length;
	}

	@Override
	public boolean areAdjacent(int n1, int n2) {
		return graph.areAdjacent(n1, n2);
	}

	@Override
	public String getName() {
		return graph.getName();
	}
	
	
	
	
}
