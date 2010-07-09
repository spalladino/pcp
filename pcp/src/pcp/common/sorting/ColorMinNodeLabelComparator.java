package pcp.common.sorting;

import java.util.Comparator;

import exceptions.AlgorithmException;

import pcp.Logger;
import pcp.algorithms.coloring.ColoringAlgorithm;


public class ColorMinNodeLabelComparator implements Comparator<Integer> {

	final private ColoringAlgorithm algorithm;
	final private boolean reverseIndex;

	public ColorMinNodeLabelComparator(ColoringAlgorithm algorithm, boolean reverseIndex) {
		this.algorithm = algorithm;
		this.reverseIndex = reverseIndex;
	}
	
	@Override
	public int compare(Integer o1, Integer o2) {
		try {
			int val = algorithm.getMinIndex(o1) - algorithm.getMinIndex(o2);
			return reverseIndex ? -val : val;
		} catch (AlgorithmException e) {
			Logger.error("Error comparing color min labels", e);
			return 0;
		}
	}

	
	
}
