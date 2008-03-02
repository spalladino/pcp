package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import pcp.Settings;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.common.iterate.ArrayIterator;
import pcp.definitions.Constants;
import pcp.definitions.Cuts;
import pcp.definitions.Sorting;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.SortedPartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.IModelData;

/**
 * Detects subsets of nodes in a graph in which every pair is either adjacent 
 * or in the same partition. 
 */
public class ExtendedCliqueCuts implements Cuts, Constants, Sorting, IBoundedAlgorithm {
	boolean checkClique = true;
	
	SortedPartitionedGraph graph;
	
	IAlgorithmSource provider;
	IAlgorithmBounder bounder;
	IModelData data;

	List<Integer> colors;
	
	int[] visited;
	int[][] edgesVisited;
	
	Node[] nodes;
	List<Node> clique;
	int color = -1;
	
	double valueWj;
	double valueSumXij;
	
	int colorCount = 0;
	int cliquesFromBrokenCount = 0;
	int cliquesFromInitialCount = 0;

	static double maxColorValue = 1.0;
	static double minColorValue = Epsilon;
	
	static double minInitialNodeValue = Settings.get().getDouble("clique.minInitialNodeValue");
	static double minCandidateNodeValue = Settings.get().getDouble("clique.minCandidateNodeValue");
	static int maxInitialNodeVisits = Settings.get().getInteger("clique.maxInitialNodeVisits");
	static int maxEdgeVisits = Settings.get().getInteger("clique.maxEdgeVisits");
	static int maxColorCount = Settings.get().getInteger("clique.maxColorCount");
	static int maxCliquesFromBroken = Settings.get().getInteger("clique.maxCliquesFromBroken");
	static boolean backtrackLastCandidate = Settings.get().getBoolean("clique.backtrackLastCandidate");
	static boolean backtrackBrokenIneqs = Settings.get().getBoolean("clique.backtrackBrokenIneqs");
	static boolean enabled = Settings.get().getBoolean("clique.enabled");
	static boolean colorsAsc = Settings.get().getBoolean("clique.colorsAsc");
	
	public ExtendedCliqueCuts(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.bounder = provider.getBounder();
		this.data = provider.getData();
		this.colors = provider.getSorted().getSortedColors(colorsAsc);
	}
	
	public ExtendedCliqueCuts run() {
		if (!enabled) return this;
		bounder.start();
		for (Integer color : this.colors) {
			this.color = color;
			valueWj = data.w(color);

			if (valueWj < minColorValue) continue;
			if (valueWj > maxColorValue) continue;
			if (this.colorCount++ > maxColorCount) break;
			
			// Initializations for current color
			this.graph = provider.getSorted().getSortedGraph(color, Desc);
			this.nodes = this.graph.getNodes();
			this.visited = new int[nodes.length];
			this.edgesVisited = new int[nodes.length][nodes.length];
			
			// Iterate over every initial node
			for (int i = 0; i < nodes.length; i++) {
				Node initial = nodes[i];
				if (data.x(initial.index(), color) < minInitialNodeValue) break;
				clique = new ArrayList<Node>(nodes.length/2);
				clique(initial);
			}
		}
		bounder.stop();
		return this;
	}

	public IAlgorithmBounder getBounder() {
		return bounder;
	}

	@Override
	public Integer getIdentifier() {
		return Cliques;
	}

	public LinkedList<Node> retainFrom(LinkedList<Node> nodes, Node[] nodesToRetain, Node currentNode) {
		ListIterator<Node> it = nodes.listIterator();
		LinkedList<Node> removed = new LinkedList<Node>();
		ArrayIterator<Node> itRetain = new ArrayIterator<Node>(nodesToRetain);
		Node retainCurrent = null;
		Comparator<Node> c = getNodeComparator();
		
		while(it.hasNext()) {
			Node node = it.next();
			while((retainCurrent == null || c.compare(retainCurrent, node) < 0) && itRetain.hasNext()) {
				retainCurrent = itRetain.next();
	}
			if (retainCurrent == null || retainCurrent.index() != node.index() 
				|| !markEdgeVisited(currentNode, node)) {
				removed.add(node);
				it.remove();
			}
		}
	
		return removed;
	}

	private void clique(Node initial) {
		// Initialize clique with initial node
		clique.add(initial);
		valueSumXij = data.x(initial.index(), color);
		visited[initial.index()]++;
		
		// Candidates to be in the clique will be neighbour/copartitioned nodes to initial
		LinkedList<Node> candidates = getInitialCandidates(initial);
		
		// Iterate while we have candidates
		while (candidates.size() > 0) {
			
			// Add first candidate to clique
			Node y = candidates.poll();
			valueSumXij += data.x(y.index(), color);
			clique.add(y);
			
			// Remove from candidates those that are not in adjacents or y partition
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y), y);
			
			// Exploit the clique if it breaks the ineq, backtrack if no more candidates
			boolean broken = valueSumXij > valueWj + Epsilon; 
			if (broken && (backtrackBrokenIneqs || candidates.isEmpty())) {
				cliquesFromBrokenCount = 0;
				backtrackBreakingClique(candidates);
				if (!bounder.check()) return;
			} 
			if ((!broken || !backtrackBrokenIneqs) && backtrackLastCandidate && candidates.isEmpty() && !removed.isEmpty()) {
				clique.remove(clique.size()-1);
				valueSumXij -= data.x(y.index(), color);
				candidates = removed;
			}
		}
		
	}

	private LinkedList<Node> getInitialCandidates(Node initial) {
		LinkedList<Node> ret = new LinkedList<Node>();
		for (Node node : graph.getNeighboursPlusCopartition(initial)) {
			if (data.x(node.index(), color) < minCandidateNodeValue) {
				break;
			} else if ((++visited[node.index()]) < maxInitialNodeVisits 
				&& markEdgeVisited(initial, node)) {
				ret.add(node);
			}
		} return ret;
	}

	private boolean markEdgeVisited(Node initial, Node node) {
		edgesVisited[initial.index()][node.index()]++;
		edgesVisited[node.index()][initial.index()]++;
		
		if (edgesVisited[node.index()][initial.index()] > maxEdgeVisits) {
			return false;
		} return true;
	}

	private void backtrackBreakingClique(LinkedList<Node> candidates) {
		if (candidates.isEmpty()) {
			if (checkClique) checkCliqueValid(color);
			provider.getCutBuilder().addClique(clique, color);
			++cliquesFromBrokenCount;
		} else {
			Node y = candidates.poll();
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y), y);
			clique.add(y);
			backtrackBreakingClique(candidates);
			
			if (cliquesFromBrokenCount >= maxCliquesFromBroken) return;
			
			clique.remove(clique.size()-1);
			if (!removed.isEmpty()) {
				candidates.addAll(removed);
				backtrackBreakingClique(candidates);
				
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

	private Comparator<Node> getNodeComparator() {
		return provider.getSorted().getNodeComparator(color, Desc);
	}


}

