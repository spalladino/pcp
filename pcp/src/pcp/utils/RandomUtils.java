package pcp.utils;

import java.util.Random;

public class RandomUtils {

	private static final Random random = new Random(213645354L);
	
	public static Random getSource() {
		return random;
	}
	
	/**
	 * Returns an integer from 0 to max exclusive, the probability of index 0 being 0.5,
	 * index 1 being 0.25, index 2 0.125, and so on until max.
	 */
	public static int pickGeometrical(int max) {
		if (max <= 1) return 0;
		max--;
		
		double value = random.nextDouble();
		double threshold = 0.5;
		int current = 0;
		
		for(current = 0 ; current < max; current++) {
			if (value <= threshold) {
				return current;
			} else {
				value -= threshold;
				threshold /= 2;
			}
		} return current;
	}
	
}
