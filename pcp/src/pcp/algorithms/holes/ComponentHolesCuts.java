package pcp.algorithms.holes;

import java.util.List;

import pcp.algorithms.bounding.IAlgorithmBounder;
import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.algorithms.holes.IHolesDetector.IHoleHandler;
import pcp.definitions.Constants;
import pcp.entities.partitioned.Node;
import pcp.entities.partitioned.SortedPartitionedGraph;
import pcp.interfaces.IAlgorithmSource;
import pcp.interfaces.IModelData;
import pcp.solver.cuts.CutFamily;
import pcp.utils.DataUtils;
import pcp.utils.IntUtils;
import props.Settings;

public class ComponentHolesCuts implements Constants, IBoundedAlgorithm {

	static boolean enabled = Settings.get().getBoolean("holes.enabled");
	
	static int maxColorCount = Settings.get().getInteger("holes.maxColorCount");
	static double minColorValue = Settings.get().getDouble("holes.minColorValue");
	static double maxColorValue = Settings.get().getDouble("holes.maxColorValue");
	static double maxPerColor = Settings.get().getDouble("holes.maxPerColor");
	
	IAlgorithmSource provider;
	IModelData data;

	IHolesDetector<Node> detector;
	SortedPartitionedGraph graph;
	
	int colorCount = 0;
	double valueWj;
	int color;
	
	int holeCount;
	
	public ComponentHolesCuts(IAlgorithmSource provider) {
		super();
		this.provider = provider;
		this.data = provider.getData();
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
			
			detector.holes(new IHoleHandler<Node>() {
				public boolean handle(final List<Node> hole) {
					if (DataUtils.sumXi(hole, color, data) - Epsilon > IntUtils.floorhalf(hole.size()) * data.w(color)) {
						provider.getCutBuilder().addHole(hole, color);
						return (++holeCount <= maxPerColor);
					} return true;
				}
			}, null);
		}
		
		provider.getBounder().stop();
		return this;
	}
	
	public IAlgorithmBounder getBounder() {
		return this.provider.getBounder();
	}

	@Override
	public CutFamily getIdentifier() {
		return CutFamily.Hole;
	}
}
