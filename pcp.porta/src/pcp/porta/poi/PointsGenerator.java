package pcp.porta.poi;

import java.util.ArrayList;
import java.util.List;

import exceptions.AlgorithmException;

import pcp.entities.IPartitionedGraph;
import pcp.entities.partitioned.Node;
import pcp.model.BuilderStrategy;
import pcp.model.strategy.Partition;
import pcp.model.strategy.Symmetry;
import pcp.porta.Parameters;
import pcp.porta.model.Variable;
import pcp.porta.processing.Translator;
import porta.poi.IPointsGenerator;


public class PointsGenerator implements IPointsGenerator {
	
	private BuilderStrategy strategy;
	private IPartitionedGraph graph;
	private Parameters cardinals;
	private List<int[]> points;
	private int[] current;
	
	private int dimension;
	private Translator translator;
	
	public PointsGenerator(IPartitionedGraph graph, Parameters cardinals, BuilderStrategy strategy) throws AlgorithmException {
		this.graph = graph;
		this.strategy = strategy;
		this.cardinals = cardinals;
		this.dimension = calculateDimension();
		this.translator = new Translator(cardinals);
		
		if (strategy.getPartitionConstraints() == Partition.PaintAtLeastOne) {
			throw new AlgorithmException("PaintAtLeastOne strategy not supported.");
		}

	}
	
	public List<int[]> getPoints() {
		points = new ArrayList<int[]>();
		current = new int[cardinals.nodeCount];
		for (int i = 0; i < current.length; i++) {
			current[i] = -1;
		}
		
		paint(0);
		return points;
	}
	
	private void paint(int partitionIndex) {
		if (partitionIndex == graph.P()) {
			addCurrent();
		} else {
			for (Node node : graph.getNodes(graph.getPartitions()[partitionIndex])) {
				for (int j = 0; j < cardinals.colorCount; j++) {
					boolean canColor = true;
					for (Node neighbour : graph.getNeighbours(node)) {
						if (current[neighbour.index] == j) {
							canColor = false;
							break;
						}
					}
					if (canColor) {
						current[node.index] = j;
						paint(partitionIndex + 1);
						current[node.index] = -1;
					}
				}
			}
		}
	}
	
	private void addCurrent() {
		int[] poi = new int[dimension];
		
		for (int i = 0; i < cardinals.nodeCount; i++) {
			if (current[i] != -1) { 
				int index = translator.convertModelToPorta(new Variable(i, current[i]));
				int color = translator.convertModelToPorta(new Variable(null, current[i]));
				poi[index-1] = 1;
				poi[color-1] = 1;
			}
		}
		
		if (isCurrentOk(poi)) {
			points.add(poi);
		}
	}

	private boolean isCurrentOk(int[] poi) {
		if (cardinals.projectColors) { 
			return true;
		}
		
		// TODO: Check strategy
		if (strategy.getBreakSymmetry() == Symmetry.UseLowerLabelFirst) {
			
			boolean using = true;
			for (int j = cardinals.nodeVarsCount; j < poi.length; j++) {
				if (poi[j] == 1 && !using) {
					return false;
				} else {
					using = (poi[j] == 1);
				}
			}
		} 
		
		return true;
	}

	private Integer calculateDimension() {
		int count = cardinals.projectColors ? 0 : cardinals.colorCount;
		return cardinals.nodeVarsCount + count;
	}

	
	public int getDimension() {
		return dimension;
	}
}
