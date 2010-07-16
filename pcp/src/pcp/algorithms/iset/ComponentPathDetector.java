package pcp.algorithms.iset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import pcp.algorithms.Algorithm;
import pcp.definitions.Constants;
import pcp.definitions.Sorting;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.interfaces.IAlgorithmSource;
import pcp.solver.cuts.CutFamily;
import pcp.utils.GraphUtils;
import pcp.utils.IntUtils;
import props.Settings;

/**
 * Detects paths and holes sorting by current fractional values.
 */
public class ComponentPathDetector extends Algorithm {
	static boolean check = Settings.get().getBoolean("validate.paths");
	
	static boolean enabled = Settings.get().getBoolean("path.enabled");
	static double minColorValue = Settings.get().getDouble("path.minColorValue");
	static int maxColorCount = Settings.get().getInteger("path.maxColorCount");
	static int maxNodeVisits = Settings.get().getInteger("path.maxNodeVisits");
	static int maxInitialNodeVisits = Settings.get().getInteger("path.maxInitialNodeVisits");
	static int maxEdgeVisits = Settings.get().getInteger("path.maxEdgeVisits");
	static int minSize = Settings.get().getInteger("path.minSize");
	static double minInitialNodeValue = Settings.get().getDouble("path.minInitialNodeValue");
	
	IPartitionedGraph graph;
	int color;
	
	int[] nodeVisits;
	int[][] edgeVisits;
	boolean[] inPath;
	List<Node> path;
	List<Double> sumToEnd;
	
	double sumX;
	double valW;
	
	Node start;
	Node end;
	Node next;
	
	int holeStart;
	boolean foundHole;
	boolean foundPath;
	
	
	public ComponentPathDetector(IAlgorithmSource source) {
		super(source);
	}
	
	public ComponentPathDetector run() {
		if (!enabled) return this;
		bounder.start();
		
		inPath = new boolean[model.getNodeCount()];
		path = null;
		
		// Iterate on sorted colors until min value or max count is hit 
		for (int color = 0; 
			color < model.getColorCount() 
				&& data.w(color) > minColorValue 
				&& color < maxColorCount; 
			color++) {
			
			this.color = color;
			this.graph = provider.getSorted().getSortedGraph(color, Sorting.Desc);
			this.valW = data.w(color);
			
			this.nodeVisits = new int[model.getNodeCount()];
			this.edgeVisits = new int[model.getNodeCount()][model.getNodeCount()];
			
			for (Node node : graph.getNodes()) {
				if ((data.x(node.index, color) < minInitialNodeValue) ||
					(nodeVisits[node.index] >= maxInitialNodeVisits)) {
					break;
				} 
				
				sumX = 0;
				process(node);
			}
		}
		
		bounder.stop();
		return this;
			
	}

	private void process(Node node) {
		
		inPath = new boolean[model.getNodeCount()];
		path = new ArrayList<Node>(20);
		sumToEnd = new LinkedList<Double>();
		
		foundHole = false;
		foundPath = false;
		
		// Pick a starting node for the path
		end = start = node;
		next = null;
		addToStart(start, null);
		
		// Add nodes to the path
		while (advance() && !foundHole && !foundPath);
		
		// If it ended because it found a hole or path then report it
		if (foundHole && (path.size() - holeStart >= minSize)) {
			markNodesAsVisited();
			List<Node> hole = buildHole(holeStart);
			if (check && !GraphUtils.checkComponentHole(graph, hole)) return;
			provider.getCutBuilder().addHole(hole , color);
		} else if (foundPath && (path.size() >= minSize)) {
			markNodesAsVisited();
			if (check && !GraphUtils.checkComponentPath(graph, path)) return;
			provider.getCutBuilder().addPath(path, color);
		}
	}
		 
	private boolean isCurrentPathBroken() {
		double alpha = IntUtils.ceilhalf(path.size());
		return path.size() >= minSize &&  sumX > valW * alpha + Constants.Epsilon;
	}

	private void addToStart(Node n, Node endpoint) {
		addToPath(n, endpoint, false);
	}
	
	private void addToEnd(Node n, Node endpoint) {
		addToPath(n, endpoint, true);
	}
	
	private void addToPath(Node n, Node endpoint, boolean isEnd) {
		if (n == null) return;
		
		inPath[n.index] = true;
		double nval = data.x(n.index, color);
		sumX += nval;
		
		if (isEnd) {
			path.add(n);
			ListIterator<Double> it = sumToEnd.listIterator();
			while (it.hasNext()) {
				Double val = it.next();
				it.set(val + nval);
			} sumToEnd.add(nval);
		} else {
			path.add(0, n);
			sumToEnd.add(0, sumX);
		}
			
	}
	
	private void markNodesAsVisited() {
		Node previous = null;
		for (Node n : this.path) {
			nodeVisits[n.index]++;
			if (previous != null) {
				edgeVisits[n.index][previous.index]++;
				edgeVisits[previous.index][n.index]++;
			} previous = n;
		}
		
		
	}

	private boolean advance() {
		foundPath = isCurrentPathBroken();
		if (!foundPath) {
			next = pickFollowing(end);
			if (next != null) {
				addToEnd(next, end);
			} end = next;
			return end != null;
		}
		
		return false;
	}
	
	private Node pickFollowing(Node current) {
		if (current == null) return null;
		
		// Iterate over all neighbours but excluded and those enough visited
		for (Node n : graph.getNeighbours(current)) {
			// if (excluded == null || !excluded.equals(n)){
			if (!inPath[n.index]) {
				if ((nodeVisits[n.index] < maxNodeVisits) && (edgeVisits[current.index][n.index] < maxEdgeVisits)) {
					boolean valid = true;
					
					// Iterate backwards so a chord can generate a valid hole
					ListIterator<Node> it = path.listIterator(path.size());
					it.previous();
					while (it.hasPrevious()) {
						int index = it.previousIndex();
						Node p = it.previous();
						
						// We are checking component paths only
						if (graph.areInSamePartition(n, p)) {
							valid = false;
							break;
						}
						
						// If it is adjacent to another node in the path, then we must check if the hole is broken,
						// in which case we return it successfully so the path is closed
						if (graph.areAdjacent(n, p)) {
							if (holeWouldBeBroken(n, p, index)) {
								holeStart = index;
								foundHole = true;
								return n;
							} else {
								valid = false;
								break;
							}
						}
					} 
					
					// Return the node if it does not create any chords
					if (valid) {
						return n;
					}
				}
			}
		} 
		
		return null;
	}

	private List<Node> buildHole(int index) {
		return path.subList(index, path.size());
	}

	/**
	 * Returns whether a hole ineq would become broken by adding node "n"
	 * at the end of the path, taking into account that the first neighbour
	 * it has in the path is "p" at index "index". 
	 */
	private boolean holeWouldBeBroken(Node n, Node p, int index) {
		double sum = sumToEnd.get(index) + data.x(n.index, color);
		int holeLength = path.size() - index + 1;
		double alpha = IntUtils.floorhalf(holeLength);
		
		return sum > alpha * valW + Constants.Epsilon;
	}

	@Override
	public CutFamily getIdentifier() {
		return CutFamily.Hole;
	}
	
}
