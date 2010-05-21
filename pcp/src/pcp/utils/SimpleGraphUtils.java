package pcp.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Node;


public class SimpleGraphUtils {
	
	public static boolean checkClique(ISimpleGraph graph, List<Node> clique) {
		for (int i = 0; i < clique.size(); i++) {
			for (int j = i+1; j < clique.size(); j++) {
				if (!(graph.areAdjacent(clique.get(i), clique.get(j)))) {
					System.err.println("Invalid clique: " + clique);
					System.err.println("Nodes " + clique.get(i) + " and " + clique.get(j) + " are not adjacent.");
					return false;
				}
			}
		} return true;
	}

	public static boolean checkHole(ISimpleGraph graph, List<Node> nodes) {
		return checkPath(graph, nodes) && checkHoleCloses(graph, nodes);
	}
	
	private static boolean checkHoleCloses(ISimpleGraph graph, List<Node> nodes) {
		Node first = nodes.get(0);
		Node last = nodes.get(nodes.size() - 1);
		
		if (!graph.areAdjacent(first.index(), last.index())) {
			System.err.println("Hole first and last nodes are not adjacent");
			return false;
		} return true;
	}

	public static boolean checkPath(ISimpleGraph graph, List<Node> nodes) {

		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> pending = new LinkedList<Node>(nodes);
		
		Node v0, v1, v2, v3;
		v0 = v1 = pending.poll();
		v2 = pending.poll();
		visited.add(v1);
		visited.add(v2);
		
		// First two must be adjacent
		if (v1.equals(v2) || !graph.areAdjacent(v1, v2)) {
			System.err.println("Error checking path " + listNodes(nodes));
			return false;
		}
		
		check: while(!pending.isEmpty()) {
			v3 = pending.poll();
			if (v2.equals(v3) || !graph.areAdjacent(v2, v3)) {
				System.err.println("Error checking path " + listNodes(nodes));
				return false;
			}
			
			for (Node v2adj : graph.getNeighbours(v2)) {
				if (v2adj.equals(v1)) continue;
				if (v2adj.equals(v0)) break check;
				// If we have seen this node, and it isnt the previous or the first one, error
				if (visited.contains(v2adj)) {
					System.err.println("Error checking path " + listNodes(nodes));
					return false;
				}
			}
			
			visited.add(v3);
			
			// Update vars to next iter
			v1 = v2;
			v2 = v3;
		}
		
		// Make sure we have seen all of them
		for (Node node : nodes) {
			if (!visited.contains(node)) {
				System.err.println("Unvisited nodes in path " + listNodes(nodes));
				return false;
			}
		}
		
		return true;
	}
	
	public static String listNodes(List<Node> nodes) {
        StringBuilder builder = new StringBuilder("[");
        for (Node h : nodes) {
            builder.append(h.index()).append(',');
        } builder.append("]");
        return builder.toString();
    }
	
}
