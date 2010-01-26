package pcp.common.sorting;

import java.util.LinkedList;
import java.util.List;

import pcp.entities.SortedPartitionedGraph;
import pcp.interfaces.IModelData;
import pcp.interfaces.ISortedProvider;
import pcp.model.Model;
import pcp.utils.Def;


public class SortedProvider implements ISortedProvider {

	private IModelData data;
	private Model model;

	NodeColorValueComparator ascNodeComparators[];
	NodeColorValueComparator descNodeComparators[];
	
	List<Integer> ascSortedColorsFrac;
	List<Integer> descSortedColorsFrac;
	
	List<Integer> ascSortedColors;
	List<Integer> descSortedColors;
	
	SortedPartitionedGraph ascGraphs[];
	SortedPartitionedGraph descGraphs[];
	
	public SortedProvider(Model model, IModelData data) {
		this.model = model;
		this.data = data;
		
		this.ascNodeComparators = new NodeColorValueComparator[model.getColorCount()];
		this.descNodeComparators = new NodeColorValueComparator[model.getColorCount()];
		
		this.ascGraphs = new SortedPartitionedGraph[model.getColorCount()];
		this.descGraphs = new SortedPartitionedGraph[model.getColorCount()];
	}


	@Override
	public NodeColorValueComparator getNodeComparator(int color, boolean asc) {
		NodeColorValueComparator[] comparators = asc ? ascNodeComparators : descNodeComparators;
		if (comparators[color] == null) { 
			comparators[color] = asc
				? new NodeColorValueComparator(data, color)
				: new ReverseNodeColorValueComparator(data, color);
		} return comparators[color];
	}

	@Override
	public List<Integer> getSortedColors(boolean asc) {
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
			if (!onlyFrac || (data.w(j) >= Def.Epsilon && data.w(j) <= (1-Def.Epsilon))) {
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
					null, 
					null);
		} return graphs[color];
	}

	
	
}
