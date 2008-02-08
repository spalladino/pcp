package pcp.algorithms.block;

import pcp.Settings;
import pcp.algorithms.bounding.Bounder;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.entities.Partition;
import pcp.entities.PartitionedGraph;
import pcp.interfaces.ICutBuilder;
import pcp.model.Model;

public class BlockColorTable {
	
	static boolean enabled = Settings.get().getBoolean("blockColor.enabled") && Settings.get().getBoolean("blockColor.usePool");
	static int maxColorsCount = Settings.get().getInteger("blockColor.maxColorsCount");
	
	Model model;
	
	IAlgorithmBounder bounder;

	public BlockColorTable(Model model) {
		this(model, null);
	}
	
	public BlockColorTable(Model model, IAlgorithmBounder bounder) {
		this.model = model;
		this.bounder = bounder == null ? new Bounder() : bounder;
	}
	
	public BlockColorTable makeCuts(ICutBuilder requestor) {
		if (!enabled) return this;
		PartitionedGraph graph = model.getGraph();
		bounder.start();
		
		// Check ineqs for every initial color
		for (int j0 = 0; j0 < model.getColorCount() - 1; j0++) {
			if (j0 > maxColorsCount) break;
			
			// Iterate for each partition to see if we break the ineq
			for (Partition partition : graph.getPartitions()) {
				// Sum over colors greater than the initial
				for (int j = j0 + 1; j < model.getColorCount(); j++) {
					// Add it as an initial cut
					requestor.addBlockColor(partition, j0);
				}
			}
		}
		
		bounder.stop();
		return this;
	}
	

	public IAlgorithmBounder getBounder() {
		return this.bounder;
	}

	
}
