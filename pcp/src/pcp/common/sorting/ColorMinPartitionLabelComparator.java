package pcp.common.sorting;

import java.util.Comparator;

import exceptions.AlgorithmException;

import pcp.Logger;
import pcp.algorithms.coloring.ColoringAlgorithm;


public class ColorMinPartitionLabelComparator implements Comparator<Integer> {

	final private ColoringAlgorithm algorithm;
	final private boolean reverseIndex;

	public ColorMinPartitionLabelComparator(ColoringAlgorithm algorithm, boolean reverseIndex) {
		this.algorithm = algorithm;
		this.reverseIndex = reverseIndex;
	}
	
	@Override
	public int compare(Integer o1, Integer o2) {
		try {
			int val = algorithm.getMinPartitionIndex(o1) - algorithm.getMinPartitionIndex(o2);
			return reverseIndex ? -val : val;
		} catch (AlgorithmException e) {
			Logger.error("Error comparing color min labels", e);
			return 0;
		}
	}

	
	
}
