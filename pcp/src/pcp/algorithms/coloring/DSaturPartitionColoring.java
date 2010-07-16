package pcp.algorithms.coloring;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.bounding.IterationsBounder;
import pcp.common.sorting.ColorCountCompleteComparator;
import pcp.common.sorting.ColorMinPartitionLabelComparator;
import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Edge;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.model.BuilderStrategy;
import pcp.model.strategy.Symmetry;
import pcp.solver.cuts.CutFamily;
import pcp.utils.IntUtils;
import pcp.utils.ListUtils;
import props.Settings;
import entities.TupleInt;
import exceptions.AlgorithmException;

public abstract class DSaturPartitionColoring extends ColoringAlgorithm implements IBoundedAlgorithm {
	
	protected static final boolean log = Settings.get().getBoolean("logging.coloring");
	protected static final boolean logFixed = Settings.get().getBoolean("logging.coloring.fixed");
	protected static final int logNodeBase = 1;
	
	protected static final boolean colorAdjPartitions = Settings.get().getBoolean("dsatur.colorAdjPartitions");
	protected static final boolean verify = Settings.get().getBoolean("validate.coloring");
	
	// ColorClass[i] = color assigned to node i
	protected int[] colorClass;

	// BestColorClass[i] = color assigned to node i in best solution
	protected int[] bestColorClass;
	
	// ColorAdj[i,j] = number of neighbours of i using color j (all colored by 0 initially)
	protected int[][] colorAdj;
	
	// ColorCount[i] = number of different colors used by neighbours of i
	protected int[] colorCount;
	
	// Partitions and nodes handled (cannot be picked in get next node call)
	protected boolean[] partitionsHandled;
	protected boolean[] nodesHandled;
	
	// Nodes left in each partition that can still be coloured 
	protected int[] usablePartitionNodes;
	
	// ColoredNodeInPartition[q] = index of the node colored in partition q
	protected Node[] coloredNodeInPartition;
	
	// PartitionsFixed keeps pairs (partition,color) to fix
	protected LinkedList<TupleInt> partitionsFixed;
	
	// Best number of colors found so far
	protected int bestColoring;
	
	// Lower bound for the coloring
	protected int lowerBound;
	
	// Stored solution
	protected int solution;
	
	// Forbidden[i][j] determines if color j is forbidden for node i
	protected boolean[][] forbidden;
	
	// Number of color fixed nodes
	protected int fixed = 0;
	protected int fixedColors = 0;
	
	private boolean hasrun = false;
	private int spaces = 0;
	
	public DSaturPartitionColoring(IPartitionedGraph graph) {
		super(graph);
		initFields();
	}
	
	@Override
	public Integer getChi() throws AlgorithmException {
		if (hasrun) return solution;
		if (logFixed) logFixed();
		
		bounder.start();
		solution = partitions(fixed, fixedColors);
		bounder.end();
		hasrun = true;
		
		if (BuilderStrategy.fromSettings().getBreakSymmetry().equals(Symmetry.VerticesNumber)) {
			sortSolution(new ColorCountCompleteComparator(this, true, false));
		} else if (BuilderStrategy.fromSettings().getBreakSymmetry().equals(Symmetry.MinimumNodeLabel)) {
			sortSolution(new ColorMinPartitionLabelComparator(this, false));
		}
		
		if (hasSolution() && verify) {
			new ColoringVerifier(this.graph).verify(this);
		}
		
		return solution;
	}
	
	@Override
	public void setUpperBound(int bound) {
		bestColoring = IntUtils.min(bound, bestColoring);
	}
	
	@Override
	public void setLowerBound(int bound) {
		lowerBound = IntUtils.max(lowerBound, bound);
	}
	
	@Override
	public boolean hasSolution() {
		return bestColorClass != null;
	}
	
	@Override
	public Integer getColor(int node) throws AlgorithmException {
		if (!hasrun) getChi();
		if (bestColorClass[node] == 0) return null;
		else return bestColorClass[node] - 1;
	}
	
	@Override
	public void useColor(int node, int color) throws AlgorithmException {
		if (canAssignColor(node, color+1)) {
			if (log) System.out.println("Using color " + (color + 1) + " for node " + (node + logNodeBase));
			handleNode(node);
			assignColor(node, color+1);
			fixed++;
			// TODO: Is it ok to use these values?
			fixedColors = IntUtils.max(fixedColors, color+1);
			lowerBound = IntUtils.max(lowerBound, color+1);
		} else {
			if (log) System.out.println("Cannot use color " + (color + 1) + " for node " + (node + logNodeBase));
		}
	}
	
	@Override
	public void useColorPartition(int partition, int color) throws AlgorithmException {
		if (log) System.out.println("Using color " + (color + 1) + " for partition " +  Arrays.toString(graph.getNodes(graph.getPartitions()[partition])));
		handlePartition(partition);
		partitionsFixed.add(new TupleInt(partition, color+1));
		
		// TODO: Is it ok to use these values?
		fixedColors = IntUtils.max(fixedColors, color+1);
		lowerBound = IntUtils.max(lowerBound, color+1);
	}

	@Override
	public void forbidColor(int node, int color) throws AlgorithmException {
		forbidden[node][color+1] = true;
	}
	
	@Override
	public CutFamily getIdentifier() {
		return null;
	}
	
	protected abstract Node getNextNode() throws AlgorithmException;

	private void sortSolution(Comparator<Integer> colorComparator) throws AlgorithmException {
		if (!hasSolution()) return;
		
		Integer[] colors = new Integer[solution];
		for (int j = 0; j < solution; j++) colors[j] = j;
		Arrays.sort(colors, colorComparator);
		
		// Only bestColorClass must be modified
		int[] sortedBestColorClass = new int[graph.N()];
		for (int j = 0; j < solution; j++) {
			for (int i = 0; i < graph.N(); i++) {
				if (bestColorClass[i] == colors[j]+1) {
					sortedBestColorClass[i] = j + 1;
				}
			}
		} this.bestColorClass = sortedBestColorClass;
	}

	private int partitions(int painted, int currentColor) throws AlgorithmException {
		indent();
		
		if (partitionsFixed.isEmpty()) {
			if (log) log("All partitions painted, proceeding to normal coloring");
			color(painted, currentColor);
		} else {
			// Poll tuple from the stack for current iteration
			TupleInt t = partitionsFixed.poll();
			int partition = t.getFirst();
			int color = t.getSecond();
			
			// Try using chosen color for every node in the partition
			for (Node node : graph.getNodes(graph.getPartition(partition))) {
				if (canAssignColor(node.index, color)) {
					if (log) log("Assigning color " + color + " to node " + (node.index + logNodeBase) + " in partition " + (partition + logNodeBase));
					handleNode(node.index);
					assignColor(node.index, color);
					partitions(painted+1, currentColor);
					removeColor(node.index, color);
					unhandleNode(node.index);
				}
			}
			
			// Return tuple to the stack for next iteration
			partitionsFixed.push(t);
		}
		
		unindent();
		return bestColoring;
	}

	private boolean canAssignColor(int index, int color) {
		return colorAdj[index][color] == 0 && !forbidden[index][color];
	}

	private int color(int painted, int currentColor) throws AlgorithmException {
		int j, newVal;
		int place;
		
		indent();
		
		if (currentColor >= bestColoring) {
			if (log) log("Fathoming coloring after painted " + (painted) + " with color count " + currentColor);
			unindent();
			return (currentColor);
		}
		
		if (bestColoring <= lowerBound) {
			unindent();
			return bestColoring;
		}
		
		if (painted >= graph.P()) {
			if (log) log("Leaf with current coloring " + currentColor);
			unindent();
			return (currentColor);
		}
		
		if (!bounder.iter()) {
			unindent();
			return bestColoring;
		}

		// Find node with maximum color_adj
		Node next = getNextNode();
		place = next.index; 
		handleNode(next.index);
		if (log) log("Picked node " + (place + logNodeBase));

		// Attempt using all colors from first to current max colors or partition degree
		for (j = 1; j <= IntUtils.min(currentColor, graph.getNeighbourPartitions(next).length + 1); j++) {
			if (canAssignColor(place, j)) {
				if (log) log("Painting node " + (place + logNodeBase) + " with color " + (j));
				assignColor(place, j);
				newVal = color(painted + 1, currentColor);
				tryUpdateBestColoring(newVal);
				
				if (log) log("Uncoloring " + (place + logNodeBase) + " which had " + j);
				removeColor(place, j);
				
				// Break loop if best coloring is better than current coloring count
				if (bestColoring <= currentColor) {
					if (log) log("Current coloring " + currentColor + " over best " + bestColoring);
					unhandleNode(place); 
					unindent();
					return bestColoring;
				}
				
			} else {
				if (log) log("Cannot apply color " + j + " to node " + (place + logNodeBase));
			}
		}
		
		// Use new color for this node
		if (currentColor + 1 < bestColoring && canAssignColor(place, currentColor+1)) {
			if (log) log("Painting node " + (place + logNodeBase) + " with color " + (currentColor+1) + " (new color)");
			assignColor(place, currentColor + 1);
			
			newVal = color(painted + 1, currentColor + 1);
			tryUpdateBestColoring(newVal);
			
			if (log) log("Uncoloring " + (place + logNodeBase) + " which had " + (currentColor + 1));
			removeColor(place, currentColor + 1);
		} else if (!(currentColor + 1 < bestColoring)) {
			if (log) log("Not painting node " + (place + logNodeBase) + " with color " + (currentColor+1) + " as it exceeds best coloring");
		} else {
			if (log) log("Cannot apply new color " + (currentColor + 1) + " to node " + (place + logNodeBase));
		}
		
		// Try leaving this node unpainted if this partition can still be somehow colored
		int partition = next.getPartition().index;
		if (usablePartitionNodes[partition] > 0) {
			if (log) log("Unhandling partition " + (partition + 1) + " of node " + (place + logNodeBase) + " for trying with another node");
			unhandlePartition(partition);
			newVal = color(painted, currentColor);
			tryUpdateBestColoring(newVal);
		}
		
		// Mark as unused and return
		unhandleNode(place);
		if (log) log("Unhandling node " + (place + logNodeBase));
		unindent();
		return (bestColoring);
	}

	private void tryUpdateBestColoring(int newVal) {
		if (newVal < bestColoring) {
			if (log)log("Setting new best coloring to " + newVal + ": " + Arrays.toString(colorClass));
			bounder.improved();
			bestColoring = newVal;
			bestColorClass = colorClass.clone();
		}
	}

	private void handleNode(int node) {
		int partition = graph.getNode(node).getPartition().index;
		partitionsHandled[partition] = true;
		nodesHandled[node] = true;
		usablePartitionNodes[partition]--;
	}
	
	private void handlePartition(int partition) {
		partitionsHandled[partition] = true;
	}
	
	private void unhandlePartition(int partition) {
		partitionsHandled[partition] = false;
	}
	
	private void unhandleNode(int node) {
		int partition = graph.getNode(node).getPartition().index;
		partitionsHandled[partition] = false;
		nodesHandled[node] = false;
		usablePartitionNodes[partition]++;
	}

	private void assignColor(int node, int color) throws AlgorithmException {
		assignColor(graph.getNode(node), color);
	}
	
	private void removeColor(int node, int color) throws AlgorithmException {
		removeColor(graph.getNodes()[node], color);
	}

	private void indent() {
		spaces++;
	}

	private void unindent() {
		spaces--;
	}

	private void log(String s) {
		for (int i = 0; i < spaces; i++) s = " " + s;
		System.out.println(s);
	}

	private void logFixed() {
		if (partitionsFixed.size() == 0) {
			System.out.println("No partitions fixed.");
		} else {
			System.out.println("Partitions fixed: " + ListUtils.toString(partitionsFixed));
			for (TupleInt t : partitionsFixed) {
				System.out.println(t.getFirst() + ": " + Arrays.toString(graph.getNodes(graph.getPartition(t.getFirst()))));
			}
		}
		
		if (fixed == 0) {
			System.out.println("No nodes fixed");
		} else {
			StringBuilder sb = new StringBuilder("Nodes fixed: ");
			for (int i = 0; i < colorClass.length; i++) {
				int j = colorClass[i];
				if (j != 0)  sb.append(" X" + i + ":" + j);
			} System.out.println(sb.toString());
		}

		{
			StringBuilder sb = new StringBuilder("Forbidden: ");
			boolean hasForbidden = false;
			for (int i = 0; i < graph.N(); i++) {
				for (int j = 0; j < maxColors(); j++) {
					if (forbidden[i][j]) {
						hasForbidden = true;
						sb.append(" X" + i + ":" + j);
					}
				}	
			}
			if (hasForbidden) System.out.println(sb.toString());
			else System.out.println("No nodes forbidden");
		}
		
	}

	protected void assignColor(Node node, int color) throws AlgorithmException {
		colorClass[node.index] = color;
		coloredNodeInPartition[node.getPartition().index] = node;
		
		for (Node n1 : graph.getNeighbours(node)) {
			increaseColorCount(n1, color);
		}
	}

	protected void removeColor(Node node, int color) throws AlgorithmException {
		//if (log) System.out.println("Unpainting node " + (node.index() + logNodeBase) + " of color " + color);
		colorClass[node.index] = 0;
		coloredNodeInPartition[node.getPartition().index] = null;
		
		for (Node n1 : graph.getNeighbours(node)) {
			decreaseColorCount(n1, color);
		}
	}

	protected void increaseColorCount(Node n1, int color)
			throws AlgorithmException {
		int node1 = n1.index;
		
		if (colorAdj[node1][color] == 0) {
			colorCount[node1]++;
		}
		
		colorAdj[node1][color]++;
		colorAdj[node1][0]--;
		
		if (colorAdj[node1][0] < 0) {
			throw new AlgorithmException("Error on dsatur assign color");
		}
	}

	protected void decreaseColorCount(Node n1, int color)
			throws AlgorithmException {
		int node1 = n1.index;
		
		colorAdj[node1][color]--;
		
		if (colorAdj[node1][color] == 0) {
			colorCount[node1]--;
		}
		
		if (colorAdj[node1][color] < 0) {
			throw new AlgorithmException("Error on dsatur remove color");
		}
		
		colorAdj[node1][0]++;
	}
	
	protected void initFields()  {
		this.bounder = new IterationsBounder();
		
		this.bestColoring = maxColors();
		this.colorAdj = new int[graph.N()][maxColors()];
		this.colorClass = new int[graph.N()];
		this.colorCount = new int[graph.N()+1];
		this.partitionsHandled = new boolean[graph.P()];
		this.partitionsFixed = new LinkedList<TupleInt>();
		this.nodesHandled = new boolean[graph.N()];
		this.coloredNodeInPartition = new Node[graph.P()];
		this.usablePartitionNodes = new int[graph.P()];
		this.forbidden = new boolean[graph.N()][maxColors()];
		this.lowerBound = 1;
		
		for (Partition p : graph.getPartitions()) {
			usablePartitionNodes[p.index] = graph.getNodes(p).length;
		}
		
		if (colorAdjPartitions) {
			for (Node n : graph.getNodes()) {
				colorAdj[n.index][0] = graph.getNeighbourPartitions(n).length; 
			}
		} else {
			for (Edge e : graph.getEdges()) {
				colorAdj[e.index1()][0]++;
				colorAdj[e.index2()][0]++;
			}
		}
	}
	
}
