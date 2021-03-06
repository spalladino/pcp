package pcp.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;

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
	
	public static List<Partition> simpleNodesToPartitions(IPartitionedGraph graph, List<pcp.entities.simple.Node> snodes) {
		List<Partition> partitions = new ArrayList<Partition>(snodes.size());
		for (pcp.entities.simple.Node snode : snodes) {
			partitions.add(graph.getPartition(snode.index()));
		} return partitions;
	}
	
	public static boolean checkComponentHole(IPartitionedGraph graph, List<Node> nodes) {
		return checkHole(graph, nodes) && checkComponent(graph, nodes);
	}
	
	public static boolean checkComponentPath(IPartitionedGraph graph, List<Node> nodes) {
		return checkPath(graph, nodes) && checkComponent(graph, nodes);
	}
	
	public static boolean checkComponent(IPartitionedGraph graph, List<Node> nodes) {
		Set<Partition> visited = new HashSet<Partition>();
		for (Node node : nodes) {
			if (visited.contains(node.getPartition())) {
				System.err.println("Set is not component set: " + listNodes(nodes));
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
	
	public static boolean checkHole(IPartitionedGraph graph, List<Node> nodes) {
		return checkPath(graph, nodes) && checkHoleCloses(graph, nodes);
	}
	
	private static boolean checkHoleCloses(IPartitionedGraph graph, List<Node> nodes) {
		Node first = nodes.get(0);
		Node last = nodes.get(nodes.size() - 1);
		
		if (!graph.areAdjacent(first.index, last.index)) {
			System.err.println("Hole first and last nodes are not adjacent");
			return false;
		} return true;
	}

	public static boolean checkPath(IPartitionedGraph graph, List<Node> nodes) {
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
            builder.append(h.index).append(',');
        } builder.append("]");
        return builder.toString();
    }

	/**
	 * Returns true if every node in one partition is adjacent to every node in the other one.
	 * @return true if every node in one partition is adjacent to every node in the other one.
	 */
	public static boolean areBipartite(IPartitionedGraph graph, Partition p1, Partition p2) {
		for (Node n1 : graph.getNodes(p1)) {
			for (Node n2 : graph.getNodes(p2)) {
				if (!graph.areAdjacent(n1, n2)) {
					return false;
				}
			}
		} return true;
	}
	
	public static void print(IPartitionedGraph graph, PrintStream stream) {
		stream.println("|P|= " + graph.P());
		stream.println("|N|= " + graph.N());
		
		for (Partition p : graph.getPartitions()) {
			stream.println(p.toString() + ": " + Arrays.toString(graph.getNodes(p)));
		}
		
		for (Node n : graph.getNodes()) {
			stream.println(n.toString() + ": " + Arrays.toString(graph.getNeighbours(n)));
		}
	}
	
	public static void printDegrees(IPartitionedGraph graph, PrintStream stream) {
		stream.println("<|P|,|N|>= " + graph.P() + ", " + graph.N());
		
		for (Node n : graph.getNodes()) {
			stream.println(n.toString() + ": " + graph.getNeighbours(n).length + " (" + graph.getNeighbourPartitions(n).length + ")" );
		}
	}
	
	
}
