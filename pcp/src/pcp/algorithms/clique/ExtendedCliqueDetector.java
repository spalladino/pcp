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
import pcp.interfaces.IModelData;
import pcp.utils.Def;

/**
 * Detects subsets of nodes in a graph in which every pair is either adjacent 
 * or in the same partition. 
 */
public class ExtendedCliqueDetector {

	boolean checkClique = true;
	
	SortedPartitionedGraph graph;
	
	IAlgorithmSource provider;
	IAlgorithmBounder bounder;
	IModelData data;

	List<Integer> colors;
	
	boolean[] visited;
	
	Node[] nodes;
	List<Node> clique;
	
	double valueWj;
	double valueSumXij;
	
	int colorCount = 0;
	int maxColorCount = Integer.MAX_VALUE;
	
	int cliquesFromBrokenCount = 0;
	int maxCliquesFromBroken = Integer.MAX_VALUE;
	
	double maxColorValue = 1.0;
	double minColorValue = Def.Epsilon;
	double minInitialNodeValue = 0.001;
	double minCandidateNodeValue = 0.0001;
	
	boolean backtrackLastCandidate = true;
	boolean eachVertexInAtMostOneBrokenIneq = false; // TODO: Implement true!
	
	public ExtendedCliqueDetector(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.bounder = provider.getBounder();
		this.data = provider.getData();
		this.colors = provider.getSorted().getSortedColors(Def.ASC);
	}
	
	public void run() {
		
		for (Integer color : this.colors) {
			valueWj = data.w(color);
			if (valueWj < minColorValue) continue;
			if (this.colorCount++ > maxColorCount || valueWj > maxColorValue) return; 
			
			// Initializations for current color
			this.graph = provider.getSorted().getSortedGraph(color, Def.DESC);
			this.nodes = this.graph.getNodes();
			this.visited = new boolean[nodes.length];
			
			// Iterate over every initial node
			for (int i = 0; i < nodes.length; i++) {
				if (data.x(i, color) < minInitialNodeValue) continue;
				clique = new ArrayList<Node>(nodes.length/2);
				clique(i, color);
			}
		}
		
	}

	public IAlgorithmBounder getBounder() {
		return bounder;
	}

	private void clique(int initialIndex, int color) {
		// Initialize clique with initial node
		Node initial = nodes[initialIndex];
		clique.add(initial);
		valueSumXij = data.x(initial.index(), color);
		visited[initial.index()] = true;
		
		// Candidates to be in the clique will be neighbour/copartitioned nodes to initial
		LinkedList<Node> candidates = getInitialCandidates(initial);
		
		// Iterate while we have candidates
		while (candidates.size() > 0) {
			
			if (!bounder.incIters()) return;
			
			// Add first candidate to clique
			Node y = candidates.poll();
			valueSumXij += data.x(y.index(), color);
			if (data.x(y.index(), color) < minCandidateNodeValue) return;
			clique.add(y);
			
			// Remove from candidates those that are not in adjacents or y partition
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y));
			
			// Exploit the clique if it breaks the ineq, backtrack if no more candidates
			if (valueSumXij > valueWj + Def.Epsilon) {
				cliquesFromBrokenCount = 0;
				breakingClique(candidates, color);
				if (!bounder.check()) return;
			}  else if (backtrackLastCandidate && candidates.isEmpty() && !removed.isEmpty()) {
				clique.remove(clique.size()-1);
				valueSumXij -= data.x(y.index(), color);
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

	private void breakingClique(LinkedList<Node> candidates, int color) {
		if (candidates.isEmpty()) {
			if (checkClique) checkCliqueValid(color);
			provider.getCutBuilder().addClique(clique, color);
			if (++cliquesFromBrokenCount >= maxCliquesFromBroken) return;
		} else {
			Node y = candidates.poll();
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y));
			clique.add(y);
			breakingClique(candidates, color);
			
			if (cliquesFromBrokenCount >= maxCliquesFromBroken) return;
			
			clique.remove(clique.size()-1);
			if (!removed.isEmpty()) {
				candidates.addAll(removed);
				breakingClique(candidates, color);
				
				if (cliquesFromBrokenCount >= maxCliquesFromBroken) return;
			}
		}
	}

	private void checkCliqueValid(int color) {
		
		for (int i = 0; i < clique.size(); i++) {
			for (int j = i+1; j < clique.size(); j++) {
				if (!(graph.areAdjacent(clique.get(i), clique.get(j)) ||
					graph.areInSamePartition(clique.get(i), clique.get(j)))) {
					System.err.println("Invalid clique: " + clique);
					System.err.println("Nodes " + clique.get(i) + " and " + clique.get(j) + " are not adjacent.");
				}
			}
		}
		
	}

	// TODO: Optimize based on sorting!
	@SuppressWarnings("unchecked")
	private LinkedList<Node> retainFrom(LinkedList<Node> nodes, Node[] toRetain) {
		List<Node> retain = Arrays.asList(toRetain);
		LinkedList<Node> clone = (LinkedList<Node>) nodes.clone();
		nodes.retainAll(retain);
		clone.removeAll(nodes);
		
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

