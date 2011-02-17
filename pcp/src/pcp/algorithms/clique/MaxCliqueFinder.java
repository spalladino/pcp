package pcp.algorithms.clique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.bounding.SolutionsBounder;
import pcp.common.sorting.SimpleNodeDegreeCompleteComparator;
import pcp.definitions.Constants;
import pcp.definitions.Sorting;
import pcp.entities.ISimpleGraph;
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
	
	static final boolean checkClique = Settings.get().getBoolean("validate.clique");
	static final boolean log = false;
	static final int minInitialDegree = 2;
	
	private SortedSimpleGraph graph;
	
	private IAlgorithmBounder bounder;

	private Node[] nodes;
	private ArrayList<Node> clique;
	private ArrayList<Node> bestClique;
	
	private LinkedList<Node> candidates; 
	private Comparator<Node> comparator;
	
	private int nesting = 0;
	
	public MaxCliqueFinder(ISimpleGraph graph) {
		this(graph, null);
	}
	
	public MaxCliqueFinder(ISimpleGraph graph, IAlgorithmBounder bounder) {
		super();
		this.graph = generateGraph(graph);
		this.nodes = this.graph.getNodes();
		this.bounder = bounder != null ? bounder : new SolutionsBounder("clique.gprime.initial");
	}
	
	public List<Node> getClique() {
		return this.bestClique;
	}
	
	public MaxCliqueFinder run() {
		bounder.start();

		// Iterate over every initial node
		for (int i = 0; i < nodes.length; i++) {
			if (!bounder.check()) break;
			Node initial = nodes[i];
			if (initial.getDegree() < minInitialDegree) break;
			clique = new ArrayList<Node>(nodes.length/2);
			clique.add(initial);
			candidates = getInitialCandidates(initial);
			nesting = 0;
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
			comparator = new SimpleNodeDegreeCompleteComparator(true);
		} return comparator;
	}
	
	private SortedSimpleGraph generateGraph(ISimpleGraph graph)	{
		return new SortedSimpleGraph(graph, getNodeComparator(), null);
	}


	private void log(String s) {
		for (int i = 0; i < nesting; i++) s = " " + s;
		System.out.println(s);
	}
	
	@SuppressWarnings("unchecked")
	private void clique() {
		nesting++;
		
		// If no more candidates, then update solution
		if (candidates.size() == 0) {
			checkCliqueValid();
			if (log) log("Finished processing candidates");
			if (bestClique == null || bestClique.size() < clique.size()) {
				if (log) log("Updating best solution: " + Arrays.toString((Node[]) clique.toArray(new Node[clique.size()])));
				bestClique = (ArrayList<Node>) clique.clone();
				bounder.improved();
			} nesting--; return;
		}
		
		// Check if we can get a better result
		if (bestClique != null && bestClique.size() >= clique.size() + candidates.size()) {
			if (log) log("Current clique " + clique.size() + " with " + candidates.size() + " left cannot exceed current solution " + bestClique.size());
			nesting--;
			return;
		}
		
		// Check bounder
		if (!bounder.iter()) {
			nesting--;
			return;
		}
		
		// Current status
		if (log) log("Current clique is " + Arrays.toString((Node[]) clique.toArray(new Node[clique.size()])) + " and candidates is " + Arrays.toString((Node[]) candidates.toArray(new Node[candidates.size()])));
		
		// Add first candidate to clique
		Node y = candidates.poll();
		clique.add(y);
		
		// Remove from candidates those that are not in adjacents to node y
		LinkedList<Node> removed = ListUtils.retainFromSorted(candidates, graph.getNeighbours(y), getNodeComparator());
		if (log) log("Added node " + y + " to clique, candidates is now " + ListUtils.toString(candidates) + " and removed is " + ListUtils.toString(removed));
		
		// Execute DFS
		clique();
		
		// Rollback adding y to the clique and take the other branch if there were removed nodes
		clique.remove(clique.size()-1);
		if (removed.size() > 0) {
			ListUtils.addSorted(candidates, removed, getNodeComparator());
			if (log) log("Removing node " + y + " from clique, candidates is now " + Arrays.toString((Node[]) candidates.toArray(new Node[candidates.size()])) + " after adding previously removed " + ListUtils.toString(removed));
			clique();
		}
		
		nesting--;
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

