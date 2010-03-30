package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Node;
import pcp.utils.SimpleGraphUtils;
import props.Settings;


public class CliqueCover {
	
	static final boolean checkClique = Settings.get().getBoolean("validate.clique");
	
	private final ISimpleGraph graph;
	private final Node[] nodes;
	private final Map<Node, Integer> degrees;
	private final Comparator<Node> comparator;

	public CliqueCover(ISimpleGraph graph) {
		this.graph = graph;
		this.degrees = new HashMap<Node, Integer>(graph.N());
		this.nodes = graph.getNodes().clone();
		this.comparator = new Comparator<Node>() {
			public int compare(Node o1, Node o2) {
				if (degrees.get(o1) == null) return -1;
				if (degrees.get(o2) == null) return 1;
				return degrees.get(o2) - degrees.get(o1);
			}
		};
	}
	
	/**
	 * Returns a clique coverage of the graph.
	 * @return a clique coverage of the graph.
	 */
	public List<List<Node>> cliques() {
		List<List<Node>> cliques = new ArrayList<List<Node>>();

		for (Node node : nodes) {
			degrees.put(node, node.getDegree());
		} Arrays.sort(nodes, comparator);
		
		// Iterate on all nodes available
		int i = 0;
		while (i < graph.N()) {
			Node current = nodes[i];
			List<Node> clique = new ArrayList<Node>();
			cliques.add(clique);
			addToClique(current, clique);
			
			// Iterate on the rest of the nodes in the graph
			for (int j = i+1; j < graph.N(); j++) {
				Node candidate = nodes[j];
				boolean adjacent = true;
				for (Node inClique : clique) {
					if (!graph.areAdjacent(inClique, candidate)) {
						adjacent = false; break;
					}
				} 
				if (adjacent) {
					addToClique(candidate, clique);
				}
			}
			
			// Sort them based on new degrees values
			Arrays.sort(nodes, i, nodes.length -1, comparator);
			while (i < graph.N() && degrees.get(nodes[i]) == null) i++;
		}
		
		if (checkClique) {
			for (List<Node> clique : cliques) {
				SimpleGraphUtils.checkClique(graph, clique);
			}
		}
		
		return cliques;
	}
		

	private void addToClique(Node node, List<Node> clique) {
		decreaseNeighbours(node);
		clique.add(node);
		degrees.put(node, null);
	}
	
	private void decreaseNeighbours(Node current) {
		for (Node n : current.getNeighbours()) {
			if (degrees.get(n) != null) {
				degrees.put(n, degrees.get(n)-1);
			}
		}
	}

	/**
	 * Returns an upper bound on the number of a clique cover for the graph.
	 * @return an upper bound on the number of a clique cover for the graph.
	 */
	public int count() {
		return this.cliques().size();
	}
}
