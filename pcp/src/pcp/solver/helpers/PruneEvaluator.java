package pcp.solver.helpers;

import pcp.model.Model;
import props.Settings;


public class PruneEvaluator {
	
	private static final int pruningRemaining = Settings.get().getInteger("pruning.remaining");
	private static final int pruningMinSet= Settings.get().getInteger("pruning.minset");
	private static final double pruningFrac = Settings.get().getDouble("pruning.frac");
	private static final boolean pruningEnabled = Settings.get().getBoolean("pruning.enabled");

	public static boolean shouldPrune(Model model, int nodesSet) {
		if (!pruningEnabled) return false;
		return nodesSet >= pruningMinSet &&
			((nodesSet >= (model.getGraph().P() - pruningRemaining)
			|| (pruningFrac <= ((double)nodesSet / (double)model.getGraph().P()))));
	}
	
	
}
