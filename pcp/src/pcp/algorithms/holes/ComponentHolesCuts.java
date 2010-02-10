package pcp.algorithms.holes;

import java.util.List;

import pcp.Settings;
import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.holes.IHolesDetector.IHoleHandler;
import pcp.definitions.Constants;
import pcp.definitions.Cuts;
import pcp.entities.Node;
import pcp.entities.SortedPartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.IModelData;
import pcp.utils.DataUtils;
import pcp.utils.IntUtils;

public class ComponentHolesCuts implements Constants, Cuts, IBoundedAlgorithm {

	static boolean enabled = Settings.get().getBoolean("holes.enabled");
	
	static int maxColorCount = Settings.get().getInteger("holes.maxColorCount");
	static double minColorValue = Settings.get().getDouble("holes.minColorValue");
	static double maxColorValue = Settings.get().getDouble("holes.maxColorValue");
	static double maxHolesRateToPartitionsPerColor = Settings.get().getDouble("holes.maxHolesRateToPartitionsPerColor");
	
	IAlgorithmSource provider;
	IModelData data;

	IHolesDetector detector;
	SortedPartitionedGraph graph;
	
	int colorCount = 0;
	double valueWj;
	int color;
	int maxHoles;
	
	int holeCount;
	
	public ComponentHolesCuts(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.data = provider.getData();
		this.maxHoles = (int) Math.ceil(maxHolesRateToPartitionsPerColor * provider.getModel().getGraph().P());
		this.holeCount = 0;
	}
	
	public ComponentHolesCuts run() {
		if (!enabled) return this;
		provider.getBounder().start();

		for (int c = 0; c < provider.getModel().getColorCount(); c++) {
			final Integer color = c;
			this.color = color;
			valueWj = data.w(color);

			if (valueWj < minColorValue) continue;
			if (valueWj > maxColorValue) continue;
			if (this.colorCount++ > maxColorCount) break;
			
			this.graph = provider.getSorted().getSortedGraph(color, false);
			this.detector = new ComponentHolesDetector(graph);
			
			detector.holes(new IHoleHandler() {
				public boolean handle(final List<Node> hole) {
					if (DataUtils.sumXi(hole, color, data) - Epsilon > IntUtils.floorhalf(hole.size()) * data.w(color)) {
						provider.getCutBuilder().addHole(hole, color);
						return (++holeCount <= maxHoles);
					} return true;
				}
			}, IHolesDetector.AllFilter);
		}
		
		provider.getBounder().stop();
		return this;
	}
	
	public IAlgorithmBounder getBounder() {
		return this.provider.getBounder();
	}

	@Override
	public Integer getIdentifier() {
		return Holes;
	}
}
