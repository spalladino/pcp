package pcp.algorithms.block;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.definitions.Constants;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.Partition;
import pcp.entities.partitioned.PartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.solver.cuts.CutFamily;
import props.Settings;

public class BlockColorCuts implements Constants, IBoundedAlgorithm {

	IAlgorithmSource provider;
	PartitionedGraph graph;

	static boolean enabled = Settings.get().getBoolean("blockColor.enabled") && !Settings.get().getBoolean("blockColor.usePool");
	static int maxColorsCount = Settings.get().getInteger("blockColor.maxColorsCount");
	
	public BlockColorCuts(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.graph = provider.getModel().getGraph();
	}
	
	public BlockColorCuts run() {
		if (!enabled) return this;
		provider.getBounder().start();
		
		// Check ineqs for every initial color
		for (int j0 = 0; j0 < provider.getModel().getColorCount() - 1; j0++) {
			if (j0 > maxColorsCount) break;
			double wj0 = provider.getData().w(j0);
			if (wj0 < Epsilon) break;
			
			// Iterate for each partition to see if we break the ineq
			for (Partition partition : graph.getPartitions()) {
				double sum = 0;
				// Sum over colors greater than the initial
				for (int j = j0 + 1; j < provider.getModel().getColorCount(); j++) {
					// Add all nodes in the partition for that color
					for (Node node : partition.getNodes()) {
						sum += provider.getData().x(node.index(), j);
					}  
				}
				// If broken, add it
				if (sum > Epsilon + wj0) {
					provider.getCutBuilder().addBlockColor(partition, j0);
				}
			}
		}
		
		provider.getBounder().stop();
		return this;
	}
	

	public IAlgorithmBounder getBounder() {
		return this.provider.getBounder();
	}

	@Override
	public CutFamily getIdentifier() {
		return CutFamily.BlockColor;
	}
	
}
