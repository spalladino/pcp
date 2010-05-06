package pcp.algorithms.coloring;

import java.util.List;

import exceptions.AlgorithmException;

import pcp.algorithms.bounding.TimeBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.solver.cuts.CutFamily;

public class DSaturColoring extends ColoringAlgorithm implements IBoundedAlgorithm {
	
	private List<Node> clique;
	
	private int[] order;
	private int[] colorClass;
	private int[] bestColorClass;
	private int[][] colorAdj;
	private int[] colorCount;
	
	private boolean[] handled;
	
	private int bestColoring;
	private int lowerBound;
	private int solution;
	
	boolean hasrun = false;
	
	public DSaturColoring(IPartitionedGraph graph) {
		super(graph);
	}
	
	public void setClique(List<Node> clique) {
		this.clique = clique;
		this.hasrun = false;
	}
	
	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		throw new AlgorithmException("Painting nodes not supported for " + this.getClass().getName());
	}

	
	@Override
	public Integer getChi() throws AlgorithmException {
		if (hasrun) return solution;
		
		initFields();
		colorClique();
		
		solution = color(clique.size(), clique.size());
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
	public CutFamily getIdentifier() {
		return null;
	}

	private int color(int i, int currentColor) throws AlgorithmException {
		int j, newVal;
		int k, max, place;
		
		if (currentColor >= bestColoring) return (currentColor);
		if (bestColoring <= lowerBound) return (bestColoring);

		if (i >= graph.N()) {
			return (currentColor);
		}

		if (!bounder.check()) { 
			return bestColoring;
		}
		
		max = -1;
		place = -1;
		
		for (k = 0; k < graph.N(); k++) {
			if (handled[k]) continue;
			// TODO: Check conditions in case of a tie
			if ((colorCount[k] > max) || ((colorCount[k] == max) && (colorAdj[k][0] > colorAdj[place][0]))) {
				max = colorCount[k];
				place = k;
			}
		}
		
		if (place == -1) {
			throw new AlgorithmException("Graph is disconnected.  This code needs to be updated for that case.\n");
		}
		
		order[i] = place;
		handled[place] = true;

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
					handled[place] = false;
					return (bestColoring);
				}
			}
		}
		if (currentColor + 1 < bestColoring) {
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
		
		handled[place] = false;
		return (bestColoring);
	}
	
	private void colorClique() throws AlgorithmException {
		int place = 0;
		for (Node node : this.clique) {
			order[place] = node.index();
			handled[node.index()] = true;
			place++;
			assignColor(node, place);
		}
	}
	
	private void assignColor(int node, int color) throws AlgorithmException {
		assignColor(graph.getNodes()[node], color);
	}
	
	private void assignColor(Node node, int color) throws AlgorithmException {
		colorClass[node.index()] = color;
		
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
		colorClass[node.index()] = 0;
		
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
		if (clique == null) {
			throw new AlgorithmException("Must set clique to start");
		}
		
		if (bounder == null) {
			bounder = new TimeBounder();
		} bounder.start();
		
		this.bestColoring = graph.N() + 1;
		this.colorAdj = new int[graph.N()][graph.N()];
		this.colorClass = new int[graph.N()];
		this.colorCount = new int[graph.N()];
		this.handled = new boolean[graph.N()];
		this.lowerBound = clique.size();
		this.order = new int[graph.N()];
		
		for (Edge e : graph.getEdges()) {
			colorAdj[e.index1()][0]++;
			colorAdj[e.index2()][0]++;
		}
	}
	
}
