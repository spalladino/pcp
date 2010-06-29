package pcp.common.sorting;

import java.util.Comparator;

import exceptions.AlgorithmException;

import pcp.Logger;
import pcp.algorithms.coloring.ColoringAlgorithm;


public class ColorCountCompleteComparator implements Comparator<Integer> {

	final private ColoringAlgorithm algorithm;
	final private boolean reverseIndex;
	final private boolean reverseCount;

	public ColorCountCompleteComparator(ColoringAlgorithm algorithm, boolean reverseCount, boolean reverseIndex) {
		this.algorithm = algorithm;
		this.reverseCount = reverseCount;
		this.reverseIndex = reverseIndex;
	}
	
	@Override
	public int compare(Integer o1, Integer o2) {
		try {
			int val = algorithm.getNodeCount(o1) - algorithm.getNodeCount(o2);
			if (val == 0) {
				val = algorithm.getMinIndex(o1) - algorithm.getMinIndex(o2);
				return reverseIndex ? -val : val;
			} else {
				return reverseCount ? -val : val;
			}
		} catch (AlgorithmException e) {
			Logger.error("Error comparing node counts", e);
			return 0;
		}
	}

	
	
}
