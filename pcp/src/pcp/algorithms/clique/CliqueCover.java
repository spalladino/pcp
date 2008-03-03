package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.SimpleNode;


public class CliqueCover {
	
	private final ISimpleGraph graph;
	private final SimpleNode[] nodes;
	private final Map<SimpleNode, Integer> degrees;
	private final Comparator<SimpleNode> comparator;

	public CliqueCover(ISimpleGraph graph) {
		this.graph = graph;
		this.degrees = new HashMap<SimpleNode, Integer>(graph.N());
		this.nodes = graph.getNodes().clone();
		this.comparator = new Comparator<SimpleNode>() {
			public int compare(SimpleNode o1, SimpleNode o2) {
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
	public List<List<SimpleNode>> cliques() {
		List<List<SimpleNode>> cliques = new ArrayList<List<SimpleNode>>();

		for (SimpleNode node : nodes) {
			degrees.put(node, node.getDegree());
		} Arrays.sort(nodes, comparator);
		
		// Iterate on all nodes available
		int i = 0;
		while (i < graph.N()) {
			SimpleNode current = nodes[i];
			List<SimpleNode> clique = new ArrayList<SimpleNode>();
			cliques.add(clique);
			addToClique(current, clique);
			
			// Iterate on the rest of the nodes in the graph
			for (int j = i+1; j < graph.N(); j++) {
				SimpleNode candidate = nodes[j];
				boolean adjacent = true;
				for (SimpleNode inClique : clique) {
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
		
		return cliques;
	}
		

	private void addToClique(SimpleNode node, List<SimpleNode> clique) {
		decreaseNeighbours(node);
		clique.add(node);
		degrees.put(node, null);
	}
	
	private void decreaseNeighbours(SimpleNode current) {
		for (SimpleNode n : current.getNeighbours()) {
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
