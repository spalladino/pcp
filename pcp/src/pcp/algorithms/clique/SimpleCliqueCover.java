package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import pcp.common.sorting.SimpleNodeDegreeComparator;
import pcp.definitions.Sorting;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.SimpleNode;

@Deprecated
public class SimpleCliqueCover {

	ISimpleGraph graph;

	public SimpleCliqueCover(ISimpleGraph graph) {
		this.graph = graph;
	}
	
	/**
	 * Returns a clique coverage of the graph.
	 * @return a clique coverage of the graph.
	 */
	public List<List<SimpleNode>> cliques() {
		List<List<SimpleNode>> cliques = new ArrayList<List<SimpleNode>>();
		Queue<SimpleNode> queue = new PriorityQueue<SimpleNode>(graph.N(), new SimpleNodeDegreeComparator(Sorting.Asc)); 
		Collections.addAll(queue, graph.getNodes());
		
		// Iterate over all nodes
		while (!queue.isEmpty()) {
			SimpleNode node = queue.poll();
			List<SimpleNode> clique = new ArrayList<SimpleNode>();
			clique.add(node);
			
			// Check all neighbours of the initial node
			for (SimpleNode neighbour : node.getNeighbours()) {
				
				// If it has already been added to the clique, return
				if (!queue.contains(neighbour)) {
					break;
				}

				// Ensure each neighbour is adjacent to all nodes in the clique
				boolean isAdjacent = true;
				for (SimpleNode inClique : clique) {
					if (!graph.areAdjacent(inClique, neighbour)) {
						isAdjacent = false;
						break;
					} 
				} 
				
				// Add it to the clique
				if (isAdjacent) {
					queue.remove(neighbour);
					clique.add(neighbour);
				}
			} 
			
			// Add the clique to the list
			cliques.add(clique);
		}
		
		return cliques;
	}

	/**
	 * Returns an upper bound on the number of a clique cover for the graph.
	 * @return an upper bound on the number of a clique cover for the graph.
	 */
	public int count() {
		return this.cliques().size();
	}
	
}
