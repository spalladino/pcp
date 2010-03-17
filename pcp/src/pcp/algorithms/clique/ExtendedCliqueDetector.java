package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
import pcp.utils.GraphUtils;
import props.Settings;

/**
 * Detects subsets of nodes in a graph in which every pair is either adjacent 
 * or in the same partition. 
 */
public abstract class ExtendedCliqueDetector implements Cuts, Constants, Sorting, IBoundedAlgorithm {
	static final boolean checkClique = Settings.get().getBoolean("validate.cliques");
	
	protected SortedPartitionedGraph graph;
	
	protected IAlgorithmSource provider;
	protected IAlgorithmBounder bounder;
	protected IModelData data;

	protected List<Integer> colors;
	
	protected int[] visited;
	protected int[][] edgesVisited;
	
	protected Node[] nodes;
	protected List<Node> clique;
	protected int color = -1;
	
	protected int colorCount = 0;
	protected int cliquesFromBrokenCount = 0;
	protected int cliquesFromInitialCount = 0;

	static int maxInitialNodeVisits = Settings.get().getInteger("clique.maxInitialNodeVisits");
	static int maxEdgeVisits = Settings.get().getInteger("clique.maxEdgeVisits");
	static int maxColorCount = Settings.get().getInteger("clique.maxColorCount");
	static int maxCliquesFromBroken = Settings.get().getInteger("clique.maxCliquesFromBroken");
	static boolean backtrackLastCandidate = Settings.get().getBoolean("clique.backtrackLastCandidate");
	static boolean backtrackBrokenIneqs = Settings.get().getBoolean("clique.backtrackBrokenIneqs");
	static boolean enabled = Settings.get().getBoolean("clique.enabled");
	static boolean colorsAsc = Settings.get().getBoolean("clique.colorsAsc");
	
	public ExtendedCliqueDetector(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.bounder = provider.getBounder();
		this.data = provider.getData();
		this.colors = getColors(colorsAsc); 
	}
	
	protected abstract List<Integer> getColors(boolean asc);
	
	protected abstract boolean onStartColor();
	
	protected abstract SortedPartitionedGraph generateGraph();
	
	public ExtendedCliqueDetector run() {
		if (!enabled) return this;
		bounder.start();
		for (Integer color : this.colors) {
			this.color = color;
			if (!onStartColor()) continue;
			if (this.colorCount++ > maxColorCount) break;
			
			// Initializations for current color
			this.graph = generateGraph();
			this.nodes = this.graph.getNodes();
			this.visited = new int[nodes.length];
			this.edgesVisited = new int[nodes.length][nodes.length];
			
			// Iterate over every initial node
			for (int i = 0; i < nodes.length; i++) {
				Node initial = nodes[i];
				if (!onInitialNode(initial)) break;
				clique(initial);
			}
		}
		bounder.stop();
		return this;
	}

	protected abstract boolean onInitialNode(Node initial);

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

	protected abstract void onAddedCandidate(Node y);
	
	protected abstract void onRemovedCandidate(Node y);
	
	protected abstract boolean isCliqueExploitable();
	
	protected abstract boolean isInvalidInitialCandidate(Node node);

	protected abstract Comparator<Node> getNodeComparator();

	private void clique(Node initial) {
		// Initialize clique with initial node
		clique = new ArrayList<Node>(nodes.length/2);
		clique.add(initial);
		visited[initial.index()]++;
		
		// Candidates to be in the clique will be neighbour/copartitioned nodes to initial
		LinkedList<Node> candidates = getInitialCandidates(initial);
		
		// Iterate while we have candidates
		while (candidates.size() > 0) {
			
			// Add first candidate to clique
			Node y = candidates.poll();
			clique.add(y);
			onAddedCandidate(y);
			
			// Remove from candidates those that are not in adjacents or y partition
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y), y);
			
			// Exploit the clique if it breaks the ineq, backtrack if no more candidates
			boolean broken = isCliqueExploitable(); 
			if (broken && (backtrackBrokenIneqs || candidates.isEmpty())) {
				cliquesFromBrokenCount = 0;
				backtrackBreakingClique(candidates);
				if (!bounder.check()) return;
			} 
			if ((!broken || !backtrackBrokenIneqs) && backtrackLastCandidate && candidates.isEmpty() && !removed.isEmpty()) {
				clique.remove(clique.size()-1);
				candidates = removed;
				onRemovedCandidate(y);
			}
		}
		
	}

	private LinkedList<Node> getInitialCandidates(Node initial) {
		LinkedList<Node> ret = new LinkedList<Node>();
		for (Node node : graph.getNeighboursPlusCopartition(initial)) {
			if (isInvalidInitialCandidate(node)) {
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
			if (checkClique) checkCliqueValid();
			provider.getCutBuilder().addClique(clique, color);
			++cliquesFromBrokenCount;
		} else {
			Node y = candidates.poll();
			LinkedList<Node> removed = retainFrom(candidates, graph.getNeighboursPlusCopartition(y), y);
			clique.add(y);
			backtrackBreakingClique(candidates);
			
			if (cliquesFromBrokenCount >= maxCliquesFromBroken) 
				return;
			
			clique.remove(clique.size()-1);
			if (!removed.isEmpty()) {
				candidates.addAll(removed);
				backtrackBreakingClique(candidates);
				
				if (cliquesFromBrokenCount >= maxCliquesFromBroken) 
					return;
			}
		}
	}

	private void checkCliqueValid() {
		GraphUtils.checkClique(graph, clique);
	}


}

