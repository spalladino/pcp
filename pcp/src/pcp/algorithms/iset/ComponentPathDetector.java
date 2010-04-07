package pcp.algorithms.iset;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pcp.algorithms.Algorithm;
import pcp.definitions.Sorting;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.interfaces.IAlgorithmSource;
import pcp.utils.IntUtils;
import props.Settings;

/**
 * Detects paths and holes sorting by current fractional values.
 */
public class ComponentPathDetector extends Algorithm  {

	static boolean enabled = Settings.get().getBoolean("path.enabled");
	static double minColorValue = Settings.get().getDouble("path.minColorValue");
	static int maxColorCount = Settings.get().getInteger("path.maxColorCount");
	static int maxNodeVisits = Settings.get().getInteger("path.maxNodeVisits");
	static int maxEdgeVisits = Settings.get().getInteger("path.maxEdgeVisits");
	static double minInitialNodeValue = Settings.get().getDouble("path.minInitialNodeValue");
	
	IPartitionedGraph graph;
	int color;
	
	int[] nodeVisits;
	int[][] edgeVisits;
	boolean[] inPath;
	List<Node> path;
	List<Node> hole;
	List<Double> sumToEnd;
	
	double sumX;
	double valW;
	
	Node start;
	Node end;
	Node next;
	
	boolean foundHole;
	boolean foundPath;
	
	
	public ComponentPathDetector(IAlgorithmSource source) {
		super(source);
	}
	
	public ComponentPathDetector run() {
		if (!enabled) return this;
		bounder.start();
		
		// init here?
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
				if (data.x(node.index(), color) < minInitialNodeValue) {
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

		foundHole = false;
		foundPath = false;
		hole = null;
		
		// Pick a starting node for the path
		end = start = node;
		next = null;
		addToStart(start, null);
		
		// Add nodes to the path
		while (next != null && !foundHole && !foundPath) {
			addToEnd(next, end);
			advance();
		}
		
		// If it ended because it found a hole or path then report it
		if (foundHole) {
			provider.getCutBuilder().addHole(hole, color);
		} else if (foundPath) {
			provider.getCutBuilder().addPath(path, color);
		}
	}
		 
	private boolean isCurrentPathBroken() {
		double alpha = IntUtils.floorhalf(path.size());
		return sumX < valW * alpha;
	}

	private void addToStart(Node n, Node endpoint) {
		addToPath(n, endpoint, false);
	}
	
	private void addToEnd(Node n, Node endpoint) {
		addToPath(n, endpoint, true);
	}
	
	private void addToPath(Node n, Node endpoint, boolean isEnd) {
		if (n == null) return;
		
		inPath[n.index()] = true;
		double nval = data.x(n.index(), color);
		sumX += nval;
		
		nodeVisits[n.index()]++;
		if (endpoint != null) {
			edgeVisits[n.index()][endpoint.index()]++;
			edgeVisits[endpoint.index()][n.index()]++;
		}
		
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

	private void advance() {
		foundPath = isCurrentPathBroken();
		if (!foundPath) {
			Node prevend = end;
			end = next;
			next = pickFollowing(end, prevend);
		}
	}
	
	private Node pickFollowing(Node current, Node excluded) {
		// Iterate over all neighbours but excluded and those enough visited
		for (Node n : current.getNeighbours()) {
			if (excluded == null || !excluded.equals(n)) {
				if ((nodeVisits[n.index()] > maxNodeVisits) || (edgeVisits[current.index()][n.index()] > maxEdgeVisits)) {
					
					boolean valid = true;
					
					// Iterate backwards so a chord can generate a valid hole
					ListIterator<Node> it = path.listIterator(path.size());
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
								buildHole(n, index);
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

	private void buildHole(Node n, int index) {
		hole = path.subList(index, path.size() - 1);
		hole.add(n);
	}

	/**
	 * Returns whether a hole ineq would become broken by adding node "n"
	 * at the end of the path, taking into account that the first neighbour
	 * it has in the path is "p" at index "index". 
	 */
	private boolean holeWouldBeBroken(Node n, Node p, int index) {
		double sum = sumToEnd.get(index) + data.x(n.index(), color);
		int holeLength = path.size() - index + 1;
		double alpha = IntUtils.floorhalf(holeLength);
		
		return sum > alpha * valW;
	}
	
}
