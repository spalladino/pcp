package pcp.algorithms.block;

import pcp.Settings;
import pcp.entities.Node;
import pcp.entities.Partition;
import pcp.entities.PartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.utils.Def;

public class BlockColor {

	IAlgorithmSource provider;
	PartitionedGraph graph;

	static boolean enabled = Settings.get().getBoolean("blockColor.enabled");
	
	static int maxColorsCount = Settings.get().getInteger("blockColor.maxColorsCount");
	
	public BlockColor(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.graph = provider.getModel().getGraph();
	}
	
	// TODO: Should we keep these ones in a pool?
	public void run() {
		if (!enabled) return;
		provider.getBounder().start();
		
		// Check ineqs for every initial color
		for (int j0 = 0; j0 < provider.getModel().getColorCount() - 1; j0++) {
			if (j0 > maxColorsCount) break;
			double wj0 = provider.getData().w(j0);
			if (wj0 < Def.Epsilon) break;
			
			// Iterate for each partition to see if we break the ineq
			for (Partition partition : graph.getPartitions()) {
				double sum = 0;
				// Sum over colors greater than the initial
				for (int j = j0 + 1; j < provider.getModel().getColorCount(); j++) {
					// Add all nodes in the partition for that color
					// TODO: When to break? iterSum == 0 too early?
					double iterSum = 0;
					for (Node node : partition.getNodes()) {
						iterSum += provider.getData().x(node.index(), j);
					} sum += iterSum; 
				}
				// TODO: Add ineq if broken
			}
		}
		provider.getBounder().stop();
	}
	
}
