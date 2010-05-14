package pcp.solver.heur;

import java.util.Map;

import exceptions.AlgorithmException;

import pcp.algorithms.coloring.ColoringAlgorithm;
import props.Settings;

public class HeuristicMetrics {

	static final boolean log = Settings.get().getBoolean("logging.callback.heuristic");
	
	int successfulLeafHeurs = 0;
	int unsuccessfulLeafHeurs = 0;
	
	long successfulLeafHeursTime = 0;
	long unsuccessfulLeafHeursTime = 0;
	
	public void leafHeur(ColoringAlgorithm coloring, int nodesSet) throws AlgorithmException {
		if (coloring.hasSolution()) {
			successfulLeafHeurs++;
			successfulLeafHeursTime += coloring.getBounder().getMillis();
			if (log) System.out.println("Used brute force at " + nodesSet + " nodes set returning coloring of " + coloring.getChi());
		} else {
			unsuccessfulLeafHeurs++;
			unsuccessfulLeafHeursTime += coloring.getBounder().getMillis();
			System.out.println("Used brute force at " + nodesSet + " nodes set without obtaining better solution");
		}
	}
	
	public void fillData(Map<String, Object> data) {
		// TODO: Fill the data
	}
	
}
