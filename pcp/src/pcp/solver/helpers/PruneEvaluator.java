package pcp.solver.helpers;

import pcp.model.Model;
import props.Settings;


public class PruneEvaluator {
	
	private static final int pruningRemaining = Settings.get().getInteger("pruning.remaining");
	private static final int pruningMinSet= Settings.get().getInteger("pruning.minset");
	private static final double pruningFrac = Settings.get().getDouble("pruning.frac");

	private Model model;
	
	public PruneEvaluator(Model model) {
		this.model = model;
	}
	
	public boolean shouldPrune(int nodesSet) {
		return nodesSet >= pruningMinSet &&
			((nodesSet >= (model.getGraph().P() - pruningRemaining)
			|| (pruningFrac <= ((double)nodesSet / (double)model.getGraph().P()))));
	}
	
	
}
