package pcp.common.sorting;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import pcp.definitions.Constants;
import pcp.entities.Edge;
import pcp.entities.SortedPartitionedGraph;
import pcp.interfaces.IModelData;
import pcp.interfaces.ISortedProvider;
import pcp.model.Model;


public class SortedProvider implements ISortedProvider, Constants {

	private IModelData data;
	private Model model;
	
	List<Integer> ascSortedColorsFrac;
	List<Integer> descSortedColorsFrac;
	
	List<Integer> ascSortedColors;
	List<Integer> descSortedColors;
	
	SortedPartitionedGraph ascGraphs[];
	SortedPartitionedGraph descGraphs[];
	
	public SortedProvider(Model model, IModelData data) {
		this.model = model;
		this.data = data;
		
		this.ascGraphs = new SortedPartitionedGraph[model.getColorCount()];
		this.descGraphs = new SortedPartitionedGraph[model.getColorCount()];
	}


	@Override
	public NodeColorValueComparator getNodeComparator(int color, boolean asc) {
		return asc
			? new NodeColorValueComparator(data, color)
			: new ReverseNodeColorValueComparator(data, color);
	}

	@Override
	public Comparator<Edge> getEdgeComparator(int color, boolean asc) {
		Comparator<Edge> comparator = new EdgeColorValueComparator(data, color);
		if (!asc) {
			comparator = new ReverseComparator<Edge>(comparator);
		} return comparator;
	}


	@Override
	public List<Integer> getSortedColors(boolean asc) {
		return getSortedColors(asc, false);
	}
	
	@Override
	public List<Integer> getSortedColors(boolean asc, boolean onlyFrac) {
		if (onlyFrac) {
			if (asc) {
				if (ascSortedColorsFrac == null) { 
					ascSortedColorsFrac = new LinkedList<Integer>();
					fillSortedColors((LinkedList<Integer>)ascSortedColorsFrac, true, false);
				} return ascSortedColorsFrac;
			} else {
				if (descSortedColorsFrac == null) { 
					descSortedColorsFrac= new LinkedList<Integer>();
					fillSortedColors((LinkedList<Integer>)descSortedColorsFrac, false, false);
				} return descSortedColorsFrac;
			}
		} else {
			if (asc) {
				if (ascSortedColors == null) { 
					ascSortedColors = new LinkedList<Integer>();
					fillSortedColors((LinkedList<Integer>)ascSortedColors, true, false);
				} return ascSortedColors;
			} else {
				if (descSortedColors == null) { 
					descSortedColors= new LinkedList<Integer>();
					fillSortedColors((LinkedList<Integer>)descSortedColors, false, false);
				} return descSortedColors;
			}
		}
	}
	
	@Override
	public List<Integer> getSortedFractionalColors(boolean asc) {
		if (asc) {
			if (ascSortedColorsFrac == null) { 
				ascSortedColorsFrac = new LinkedList<Integer>();
				fillSortedColors((LinkedList<Integer>)ascSortedColorsFrac, true, true);
			} return ascSortedColorsFrac;
		} else {
			if (descSortedColorsFrac == null) { 
				descSortedColorsFrac = new LinkedList<Integer>();
				fillSortedColors((LinkedList<Integer>)descSortedColorsFrac, false, true);
			} return descSortedColorsFrac;
		}
	}
	
	public void fillSortedColors(LinkedList<Integer> list, boolean asc, boolean onlyFrac) {
		for (int j = 0; j < model.getColorCount(); j++) {
			if (!onlyFrac || (data.w(j) >= Epsilon && data.w(j) <= (1-Epsilon))) {
				int index = 0;
				while (index < list.size() && (asc 
						? data.w(list.get(index)) <= data.w(j) 
						: data.w(list.get(index)) >= data.w(j))) {
					index++;
				} list.add(index, j);
			} 
		}
	}
	

	@Override
	public SortedPartitionedGraph getSortedGraph(int color, boolean asc) {
		SortedPartitionedGraph[] graphs = asc ? ascGraphs : descGraphs;
		if (graphs[color] == null) {
			graphs[color] = new SortedPartitionedGraph(model.getGraph(), 
					getNodeComparator(color, asc), 
					getEdgeComparator(color, asc), 
					null);
		} return graphs[color];
	}

	
	
}
