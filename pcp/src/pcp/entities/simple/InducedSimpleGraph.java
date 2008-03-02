package pcp.entities.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.entities.ISimpleGraph;


public class InducedSimpleGraph implements ISimpleGraph {
	
	ISimpleGraph graph;

	Map<Integer, SimpleNode> nodesMap;
	Map<Integer, SimpleNode[]> nodesAdjacencies;

	public InducedSimpleGraph(ISimpleGraph graph, SimpleNode[] nodes) {
		this.graph = graph;
		
		this.nodesAdjacencies = new HashMap<Integer, SimpleNode[]>(nodes.length);
		this.nodesMap = new HashMap<Integer, SimpleNode>(nodes.length);
		for (SimpleNode node : nodes) {
			this.nodesMap.put(node.name, new SimpleNode(this, node.name));
		}
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
		Collection<SimpleNode> values = nodesMap.values();
		return (SimpleNode[]) values.toArray(new SimpleNode[values.size()]);
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
	
	
	
	
}
