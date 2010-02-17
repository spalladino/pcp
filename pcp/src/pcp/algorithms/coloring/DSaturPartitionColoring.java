package pcp.algorithms.coloring;

import pcp.algorithms.AlgorithmException;
import pcp.algorithms.bounding.Bounder;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.Edge;
import pcp.entities.Node;
import pcp.interfaces.IPartitionedGraph;

public class DSaturPartitionColoring extends Coloring implements IBoundedAlgorithm {
	
	private static final boolean log = true;
	
	private IAlgorithmBounder bounder;
	
	// ColorClass[i] = color assigned to node i
	private int[] colorClass;

	// BestColorClass[i] = color assigned to node i in best solution
	private int[] bestColorClass;
	
	// ColorAdj[i,j] = number of neighbours of i using color j (all colored by 0 initially)
	private int[][] colorAdj;
	
	// ColorCount[i] = number of different colors used by neighbours of i
	private int[] colorCount;
	
	// Handled[q] = partition q has been colored
	private boolean[] handled;
	
	// ColoredNodeInPartition[q] = index of the node colored in partition q
	private Node[] coloredNodeInPartition;
	
	// Best number of colors found so far
	private int bestColoring;
	
	// Lower bound for the coloring
	private int lowerBound;
	
	// Stored solution
	private int solution;
	
	private boolean hasrun = false;
	
	public DSaturPartitionColoring(IPartitionedGraph graph) {
		super(graph);
	}
	
	@Override
	public IAlgorithmBounder getBounder() {
		return this.bounder;
	}
	
	public void setBounder(IAlgorithmBounder bounder) {
		this.bounder = bounder;
		this.hasrun = false;
	}
	
	@Override
	public Integer getChi() throws AlgorithmException {
		if (hasrun) return solution;
		
		initFields();
		
		solution = color(0, 0);
		bounder.end();
		hasrun = true;
		
		return solution;
	}
	
	@Override
	public Integer getColor(int node) throws AlgorithmException {
		if (!hasrun) getChi();
		return bestColorClass[node] - 1;
	}

	@Override
	public Integer getIdentifier() {
		
		return null;
	}
	
	protected Node getNextNode() {
		/*max = -1;
		place = -1;
		
		for (k = 0; k < graph.N(); k++) {
			//if (handled[k.partition]) continue; !!!
			if ((colorCount[k] > max) || ((colorCount[k] == max) && (colorAdj[k][0] > colorAdj[place][0]))) {
				max = colorCount[k];
				place = k;
			}
		}
		
		// Disconnected graphs unhandled
		if (place == -1) {
			throw new AlgorithmException("Graph is disconnected. This code needs to be updated for that case.\n");
		}
		*/
		return null;
	}

	private int color(int i, int currentColor) throws AlgorithmException {
		int j, newVal;
		int place, partition;
		
		if (currentColor >= bestColoring) {
			if (log) System.out.println("Fathoming coloring for node " + i + " with color " + currentColor);
			return (currentColor);
		}
		if (bestColoring <= lowerBound) {
			return (bestColoring);
		}
		if (i >= graph.N()) {
			if (log) System.out.println("Leaf with current coloring " + currentColor);
			return (currentColor);
		}
		if (!bounder.check()) { 
			return bestColoring;
		}

		// Find node with maximum color_adj
		Node next = getNextNode();
		place = next.index(); 
		partition = next.getPartition().index(); 
		handled[partition] = true;

		// Execute DFS
		for (j = 1; j <= currentColor; j++) {
			if (!bounder.check()) {
				return bestColoring;
			}
			if (colorAdj[place][j] == 0) {
				assignColor(place, j);
				newVal = color(i + 1, currentColor);
				if (newVal < bestColoring) {
					bestColoring = newVal;
					bestColorClass = colorClass.clone();
					if (!bounder.check()) {
						return bestColoring;
					}
				}
				
				removeColor(place, j);
				if (bestColoring <= currentColor) {
					if (log) System.out.println("Current coloring " + currentColor + " over best " + bestColoring);
					handled[partition] = false;
					return bestColoring;
				}
			}
		}
		if (currentColor + 1 < bestColoring) {
			if (log) System.out.println("Attempting to color " + place + " with color next to " + currentColor);
			assignColor(place, currentColor + 1);
			
			newVal = color(i + 1, currentColor + 1);
			if (newVal < bestColoring) {
				bestColoring = newVal;
				bestColorClass = colorClass.clone();
				if (!bounder.check()) {
					return bestColoring;
				}
			}
			
			removeColor(place, currentColor + 1);
		}
		
		handled[partition] = false;
		return (bestColoring);
	}

	private void assignColor(int node, int color) throws AlgorithmException {
		assignColor(graph.getNode(node), color);
	}
	
	private void assignColor(Node node, int color) throws AlgorithmException {
		if (log) System.out.println("Painting node " + node + " with color " + color);
		colorClass[node.index()] = color;
		coloredNodeInPartition[node.getPartition().index()] = node;
		
		for (Node n1 : node.getNeighbours()) {
			int node1 = n1.index();
			
			if (colorAdj[node1][color] == 0) {
				colorCount[node1]++;
			}
			
			colorAdj[node1][color]++;
			colorAdj[node1][0]--;
			
			if (colorAdj[node1][0] < 0) {
				throw new AlgorithmException("Error on dsatur assign color");
			}
		}
	}
	
	private void removeColor(int node, int color) throws AlgorithmException {
		removeColor(graph.getNodes()[node], color);
	}
	
	private void removeColor(Node node, int color) throws AlgorithmException {
		if (log) System.out.println("Unpainting node " + node + " of color " + color);
		colorClass[node.index()] = 0;
		coloredNodeInPartition[node.getPartition().index()] = null;
		
		for (Node n1 : node.getNeighbours()) {
			int node1 = n1.index();
			
			colorAdj[node1][color]--;
			
			if (colorAdj[node1][color] == 0) {
				colorCount[node1]--;
			}
			
			if (colorAdj[node1][color] < 0) {
				throw new AlgorithmException("Error on dsatur remove color");
			}
			
			colorAdj[node1][0]++;
		}
	}
	
	private void initFields() throws AlgorithmException {
		if (bounder == null) {
			bounder = new Bounder();
		} bounder.start();
		
		this.bestColoring = graph.N() + 1;
		this.colorAdj = new int[graph.N()][graph.N()];
		this.colorClass = new int[graph.N()];
		this.colorCount = new int[graph.N()];
		this.handled = new boolean[graph.P()];
		this.coloredNodeInPartition = new Node[graph.P()];
		this.lowerBound = -1;
		
		for (Edge e : graph.getEdges()) {
			colorAdj[e.index1()][0]++;
			colorAdj[e.index2()][0]++;
		}
	}
	
}
