package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.common.iterate.ArrayIterator;
import pcp.common.sorting.SimpleNodeDegreeComparator;
import pcp.definitions.Constants;
import pcp.definitions.Sorting;
import pcp.entities.simple.Graph;
import pcp.entities.simple.Node;
import pcp.entities.simple.SortedSimpleGraph;
import pcp.solver.cuts.CutFamily;
import pcp.utils.ListUtils;
import pcp.utils.SimpleGraphUtils;
import props.Settings;

/**
 * Finds the max clique in a simple graph 
 */
public class MaxCliqueFinder implements Constants, Sorting, IBoundedAlgorithm {
	static final boolean checkClique = Settings.get().getBoolean("validate.cliques");
	
	private SortedSimpleGraph graph;
	
	private IAlgorithmBounder bounder;

	private Node[] nodes;
	private ArrayList<Node> clique;
	private ArrayList<Node> bestClique;
	
	private LinkedList<Node> candidates; 
	private Comparator<Node> comparator;
	
	public MaxCliqueFinder(Graph graph, IAlgorithmBounder bounder) {
		super();
		this.graph = generateGraph(graph);
		this.nodes = this.graph.getNodes();
		this.bounder = bounder == null ? bounder : new SolutionsBounder("clique.gprime.initial");
	}
	
	public MaxCliqueFinder run() {
		bounder.start();

		// Iterate over every initial node
		for (int i = 0; i < nodes.length; i++) {
			Node initial = nodes[i];
			clique = new ArrayList<Node>(nodes.length/2);
			clique.add(initial);
			candidates = getInitialCandidates(initial);
			clique();
		}

		bounder.stop();
		return this;
	}

	public IAlgorithmBounder getBounder() {
		return bounder;
	}

	@Override
	public CutFamily getIdentifier() {
		return CutFamily.Clique;
	}

	private Comparator<Node> getNodeComparator() { 
		if (this.comparator == null) {
			comparator = new SimpleNodeDegreeComparator(true);
		} return comparator;
	}
	
	private SortedSimpleGraph generateGraph(Graph graph)	{
		return new SortedSimpleGraph(graph, getNodeComparator(), null);
	}

	protected LinkedList<Node> retainFrom(LinkedList<Node> nodes, Node[] nodesToRetain, Node currentNode) {
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
			
			if (retainCurrent == null || retainCurrent.index() != node.index()) {
				removed.add(node);
				it.remove();
			}
		}
	
		return removed;
	}

	@SuppressWarnings("unchecked")
	private void clique() {
		
		// If no more candidates, then update solution
		if (candidates.size() == 0) {
			checkCliqueValid();
			if (bestClique == null || bestClique.size() < clique.size()) {
				bestClique = (ArrayList<Node>) clique.clone();
				bounder.improved();
			} return;
		}
		
		// Check if we can get a better result
		if (bestClique != null && bestClique.size() >= clique.size() + candidates.size()) {
			return;
		}
		
		// Check bounder
		if (!bounder.iter()) {
			return;
		}
		
		// Add first candidate to clique
		Node y = candidates.poll();
		clique.add(y);
		
		// Remove from candidates those that are not in adjacents to node y
		LinkedList<Node> removed = retainFrom(candidates, graph.getNeighbours(y), y);
		
		// Execute DFS
		clique();
		
		// Rollback adding y to the clique and take the other branch
		if (removed.size() > 0) {
			clique.remove(clique.size()-1);
			ListUtils.addSorted(candidates, removed, getNodeComparator());
			clique();
		}
	}

	private LinkedList<Node> getInitialCandidates(Node node) {
		LinkedList<Node> ret = new LinkedList<Node>();
		for (Node n : graph.getNeighbours(node)) {
			ret.add(n);
		} return ret;
	}

	private void checkCliqueValid() {
		SimpleGraphUtils.checkClique(graph, clique);
	}


}

