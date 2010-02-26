package pcp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import pcp.common.TupleInt;
import pcp.interfaces.ISimpleGraph;
import pcp.interfaces.ISimpleGraphBuilder;


public class SimpleGraphBuilder implements ISimpleGraphBuilder {

	Integer nodeCount;
	
	List<TupleInt> edges;
	Map<Integer, Set<Integer>> nodeAdjacencies;

	String name;
	
	public SimpleGraphBuilder(String name) {
		this.nodeAdjacencies = new TreeMap<Integer, Set<Integer>>();
		this.edges = new ArrayList<TupleInt>();
		this.nodeCount = 0;
		this.name = name;
	}
	
	@Override
	public ISimpleGraphBuilder addEdge(int n1, int n2) {
		edges.add(new TupleInt(n1, n2));
		nodeAdjacencies.get(n1).add(n2);
		nodeAdjacencies.get(n2).add(n1);
		return this;
	}

	@Override
	public ISimpleGraphBuilder addNode(int node) {
		nodeAdjacencies.put(node, new TreeSet<Integer>());
		nodeCount++;
		return this;
	}

	@Override
	public ISimpleGraph getGraph() {
		SimpleGraph graph = new SimpleGraph(nodeCount, edges.size());
		graph.name = name;
		
		for (int i = 0; i < nodeCount; i++) {
			graph.nodes[i] = new SimpleNode(graph, i);
		}
		
		for (int h = 0; h < edges.size(); h++) {
			TupleInt t = edges.get(h);
			graph.edges[h] = new SimpleEdge(graph.nodes[t.getFirst()], graph.nodes[t.getSecond()]);
			graph.matrix[t.getFirst()][t.getSecond()] = true;
			graph.matrix[t.getSecond()][t.getFirst()] = true;
		}
		
		for (Entry<Integer, Set<Integer>> entry : nodeAdjacencies.entrySet()) {
			SimpleNode[] neighbours = new SimpleNode[entry.getValue().size()];
			
			int idx = 0;			
			for (Integer n : entry.getValue()) {
				neighbours[idx++] = graph.nodes[n];
			} graph.adjacencies[entry.getKey()] = neighbours;  
		}
		
		return graph;
	}

	
	
}
