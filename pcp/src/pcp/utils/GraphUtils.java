package pcp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.simple.SimpleNode;

public class GraphUtils {

	public static Map<Partition, List<Node>> groupByPartition(Node[] nodes) {
		Map<Partition, List<Node>> map = new HashMap<Partition, List<Node>>();
		for (Node node : nodes) {
			Partition p = node.getPartition();
			if (!map.containsKey(p)) {
				map.put(p, new ArrayList<Node>());
			} map.get(p).add(node);
		} return map;
	}
	
	public static boolean checkComponentHole(IPartitionedGraph graph, List<Node> nodes) {
		if (!checkHole(graph, nodes)) {
			return false;
		}
		
		Set<Partition> visited = new HashSet<Partition>();
		for (Node node : nodes) {
			if (visited.contains(node.getPartition())) {
				System.err.println("Hole is not component hole: " + listNodes(nodes));
				return false;
			} visited.add(node.getPartition());
		}
		
		return true;
	}
	
	public static boolean checkClique(IPartitionedGraph graph, List<Node> clique) {
		for (int i = 0; i < clique.size(); i++) {
			for (int j = i+1; j < clique.size(); j++) {
				if (!(graph.areAdjacent(clique.get(i), clique.get(j)) ||
					graph.areInSamePartition(clique.get(i), clique.get(j)))) {
					System.err.println("Invalid clique: " + clique);
					System.err.println("Nodes " + clique.get(i) + " and " + clique.get(j) + " are not adjacent.");
					return false;
				}
			}
		} return true;
	}
	
	public static boolean checkClique(ISimpleGraph graph, List<SimpleNode> clique) {
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
	
	public static boolean checkHole(IPartitionedGraph graph, List<Node> nodes) {
		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> pending = new LinkedList<Node>(nodes);
		
		Node v0, v1, v2, v3;
		v0 = v1 = pending.poll();
		v2 = pending.poll();
		visited.add(v1);
		visited.add(v2);
		
		// First two must be adjacent
		if (v1.equals(v2) || !graph.areAdjacent(v1, v2)) {
			System.err.println("Error checking hole " + listNodes(nodes));
			return false;
		}
		
		check: while(!pending.isEmpty()) {
			v3 = pending.poll();
			if (v2.equals(v3) || !graph.areAdjacent(v2, v3)) {
				System.err.println("Error checking hole " + listNodes(nodes));
				return false;
			}
			
			for (Node v2adj : graph.getNeighbours(v2)) {
				if (v2adj.equals(v1)) continue;
				if (v2adj.equals(v0)) break check;
				// If we have seen this node, and it isnt the previous or the first one, error
				if (visited.contains(v2adj)) {
					System.err.println("Error checking hole " + listNodes(nodes));
					return false;
				}
			}
			
			visited.add(v3);
			
			// Update vars to next iter
			v1 = v2;
			v2 = v3;
		}
		
		// Last two must be adjacent
		if (v0.equals(v2) || !graph.areAdjacent(v0, v2)) {
			System.err.println("Error checking hole " + listNodes(nodes));
			return false;
		}
		
		// Make sure we have seen all of them
		for (Node node : nodes) {
			if (!visited.contains(node)) {
				System.err.println("Unvisited nodes in hole " + listNodes(nodes));
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

	/**
	 * Returns true if every node in one partition is adjacent to every node in the other one.
	 * @return true if every node in one partition is adjacent to every node in the other one.
	 */
	public static boolean areBipartite(Partition p1, Partition p2) {
		return checkAllNodesAdjacent(p1, p2) && checkAllNodesAdjacent(p2, p1);  
	}
	
	private static boolean checkAllNodesAdjacent(Partition p1, Partition p2) {
		for (Node n1 : p1.getNodes()) {
			Set<Node> pending = new HashSet<Node>();
			Collections.addAll(pending, p2.getNodes());
			for (Node adj : n1.getNeighbours()) {
				pending.remove(adj);
			}
			if (!pending.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
}
