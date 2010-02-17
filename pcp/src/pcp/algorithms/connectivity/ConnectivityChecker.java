package pcp.algorithms.connectivity;

import java.util.LinkedList;
import java.util.Queue;

import pcp.entities.Node;
import pcp.interfaces.IPartitionedGraph;


public class ConnectivityChecker {
	
	IPartitionedGraph graph;
	boolean[] visited;
	Queue<Node> pending;
	
	public ConnectivityChecker(IPartitionedGraph graph) {
		this.graph = graph;
		this.visited = new boolean[graph.N()];
		this.pending = new LinkedList<Node>();
		
		this.pending.add(graph.getNodes()[0]);
		this.visited[0] = true;
	}
	
	public boolean check() {
		
		while(!this.pending.isEmpty()) {
			Node current = this.pending.poll();
			for (Node n : current.getNeighbours()) {
				if (!this.visited[n.index()]) {
					this.pending.add(n);
					this.visited[n.index()] = true;
				}
			}
			for (Node n : current.getPartition().getNodes()) {
				if (!this.visited[n.index()]) {
					this.pending.add(n);
					this.visited[n.index()] = true;
				}
			}
		}
		
		for (boolean nodeVisited : visited) {
			if (!nodeVisited) {
				System.err.println("Graph is disconnected");
				return false;
			}
		} return true;
	}
	
}
