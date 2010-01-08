package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.common.Predicate;
import pcp.entities.Node;
import pcp.entities.SortedPartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.utils.Def;

/**
 * Detects subsets of nodes in a graph in which every pair is either adjacent 
 * or in the same partition. 
 */
public class ExtendedCliqueDetector {

	SortedPartitionedGraph graph;
	
	IAlgorithmSource provider;
	IAlgorithmBounder bounder;

	List<Integer> colors;
	
	boolean[] visited;
	Predicate<Node> excludeVisitedIndices;
	
	Node[] nodes;
	List<Node> clique;
	
	double valueWj;
	double valueSumXij;
	
	int colorCount = 0;
	int maxColorCount = Integer.MAX_VALUE;
	
	double maxColorValue = 0.9;
	double minInitialNodeValue = 0.001;
	double minCandidateNodeValue = 0.0001;
	
	boolean backtrackLastCandidate = true;
	
	public ExtendedCliqueDetector(IAlgorithmSource provider) {
		super();
		this.bounder = provider;
		this.provider = provider;
		this.colors = provider.getSortedColors(Def.ASC, false, false);
	}
	
	public void run() throws Exception {
		
		for (Integer color : this.colors) {
			valueWj = provider.w(color);
			if (this.colorCount++ > maxColorCount || valueWj > maxColorValue) return; 
			
			// Initializations for current color
			this.graph = provider.getSortedGraph(color, Def.DESC);
			this.nodes = this.graph.getNodes();
			this.visited = new boolean[nodes.length];
			this.excludeVisitedIndices = createExcludeVisitedIndicesPredicate(visited);
			
			// Iterate over every initial node
			for (int i = 0; i < nodes.length; i++) {
				if (provider.x(i, color) < minInitialNodeValue) return;
				clique = new ArrayList<Node>(nodes.length/2);
				clique(i, color);
			}
		}
		
	}

	private void clique(int initialIndex, int color) throws Exception {
		// Initialize clique with initial node
		Node initial = nodes[initialIndex];
		clique.add(initial);
		valueSumXij = provider.x(initialIndex, color);
		visited[initialIndex] = true;
		
		// Candidates to be in the clique will be neighbour/copartitioned nodes to initial
		LinkedList<Node> candidates = getInitialCandidates(initial);
		
		// Iterate while we have candidates
		while (candidates.size() > 0) {
			
			if (!bounder.incIters()) return;
			
			// Add first candidate to clique
			Node y = candidates.poll();
			valueSumXij += provider.x(y.index(), color);
			if (provider.x(y.index(), color) < minCandidateNodeValue) return;
			clique.add(y);
			
			// Remove from candidates those that are not in adjacents or y partition
			LinkedList<Node> removed = removeFrom(candidates, graph.getNeighboursPlusCopartition(y));
			
			// Exploit the clique if it breaks the ineq, backtrack if no more candidates
			if (valueSumXij > valueWj) {
				breakingClique(candidates);
				if (!bounder.check()) return;
			}  else if (backtrackLastCandidate && candidates.isEmpty() && !removed.isEmpty()) {
				clique.remove(clique.size()-1);
				valueSumXij -= provider.x(y.index(), color);
				candidates = removed;
			}
		}
		
	}

	private LinkedList<Node> getInitialCandidates(Node initial) {
		LinkedList<Node> ret = new LinkedList<Node>();
		for (Node node : graph.getNeighboursPlusCopartition(initial)) {
			if (!visited[node.index()]) {
				ret.add(node);
			}
		} return ret;
	}

	private void breakingClique(LinkedList<Node> candidates) {
		// TODO Auto-generated method stub
		
	}

	// TODO: Optimize based on sorting!
	private LinkedList<Node> removeFrom(LinkedList<Node> nodes, Node[] toRemove) {
		List<Node> remove = Arrays.asList(toRemove);
		LinkedList<Node> clone = (LinkedList<Node>) nodes.clone();
		nodes.removeAll(remove);
		clone.retainAll(remove);
		
		return clone;
	}


	private Predicate<Node> createExcludeVisitedIndicesPredicate(final boolean[] visited) {
		return new Predicate<Node>() {
			public boolean evaluate(Node obj) {
				return visited[obj.index()];
			}
		};
	}
	

}

