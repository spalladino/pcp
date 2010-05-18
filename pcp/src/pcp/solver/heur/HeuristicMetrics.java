package pcp.solver.heur;

import java.util.Map;

import exceptions.AlgorithmException;

import pcp.algorithms.coloring.ColoringAlgorithm;
import props.Settings;

public class HeuristicMetrics {

	static final boolean log = Settings.get().getBoolean("logging.callback.heuristic");
	
	int successfulLeafHeurs = 0;
	int unsuccessfulLeafHeurs = 0;
	int successfulPrimalHeurs = 0;
	int unsuccessfulPrimalHeurs = 0;
	
	long successfulLeafHeursTime = 0;
	long unsuccessfulLeafHeursTime = 0;
	long successfulPrimalHeursTime = 0;
	long unsuccessfulPrimalHeursTime = 0;
	
	public void leafHeur(ColoringAlgorithm coloring, int nodesSet) throws AlgorithmException {
		if (coloring.hasSolution()) {
			successfulLeafHeurs++;
			successfulLeafHeursTime += coloring.getBounder().getMillis();
			if (log) System.out.println("Used brute force at " + nodesSet + " nodes set returning coloring of " + coloring.getChi());
		} else {
			unsuccessfulLeafHeurs++;
			unsuccessfulLeafHeursTime += coloring.getBounder().getMillis();
			if (log) System.out.println("Used brute force at " + nodesSet + " nodes set without obtaining better solution");
		}
	}

	public void primalHeur(ColoringAlgorithm coloring, int nodeNumber, int fixed) throws AlgorithmException {
		if (coloring.hasSolution()) {
			successfulPrimalHeurs++;
			successfulPrimalHeursTime += coloring.getBounder().getMillis();
			if (log) System.out.println("Used primal heuristic at node " + nodeNumber + " having fixed " + fixed + " returning coloring of " + coloring.getChi());
		} else {
			unsuccessfulPrimalHeurs++;
			unsuccessfulPrimalHeursTime += coloring.getBounder().getMillis();
			if (log) System.out.println("Used primal heuristic at node " + nodeNumber + " having fixed " + fixed + " without obtaining better solution");
		}
	}

	
	public void fillData(Map<String, Object> data) {
		data.put("solution.leafheur.success.count", successfulLeafHeurs);
		data.put("solution.leafheur.success.time", successfulLeafHeursTime);
		data.put("solution.leafheur.unsuccess.count", unsuccessfulLeafHeurs);
		data.put("solution.leafheur.unsuccess.time", unsuccessfulLeafHeursTime);
		data.put("solution.leafheur.count", successfulLeafHeurs + unsuccessfulLeafHeurs);
		data.put("solution.leafheur.time", successfulLeafHeursTime + unsuccessfulLeafHeursTime);
		data.put("solution.primalheur.success.count", successfulPrimalHeurs);
		data.put("solution.primalheur.success.time", successfulPrimalHeursTime);
		data.put("solution.primalheur.unsuccess.count", unsuccessfulPrimalHeurs);
		data.put("solution.primalheur.unsuccess.time", unsuccessfulPrimalHeursTime);
		data.put("solution.primalheur.count", successfulPrimalHeurs + unsuccessfulPrimalHeurs);
		data.put("solution.primalheur.time", successfulPrimalHeursTime + unsuccessfulPrimalHeursTime);
	}
	
	public void printTotal() {
		System.out.println("Heuristic callback");
		System.out.println(" Successfully ran leaf heuristic " + (successfulLeafHeurs) + " times for " + (successfulLeafHeursTime) + " millis");
		System.out.println(" Unsuccessfully ran leaf heuristic " + (unsuccessfulLeafHeurs) + " times for " + (unsuccessfulLeafHeursTime) + " millis");
		System.out.println(" Total ran leaf heuristic " + (successfulLeafHeurs + unsuccessfulLeafHeurs) + " times for " + (successfulLeafHeursTime + unsuccessfulLeafHeursTime) + " millis");
		System.out.println(" Successfully ran primal heuristic " + (successfulPrimalHeurs) + " times for " + (successfulPrimalHeursTime) + " millis");
		System.out.println(" Unsuccessfully ran primal heuristic " + (unsuccessfulPrimalHeurs) + " times for " + (unsuccessfulPrimalHeursTime) + " millis");
		System.out.println(" Total ran primal heuristic " + (successfulPrimalHeurs + unsuccessfulPrimalHeurs) + " times for " + (successfulPrimalHeursTime + unsuccessfulPrimalHeursTime) + " millis");
	}

	
}
