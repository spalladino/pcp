package pcp.algorithms.holes;

import java.util.List;

import pcp.algorithms.Algorithm;
import pcp.algorithms.holes.IHolesDetector.IHoleHandler;
import pcp.definitions.Constants;
import pcp.entities.IPartitionedGraph;
import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Node;
import pcp.interfaces.IAlgorithmSource;
import pcp.solver.cuts.CutFamily;
import pcp.utils.IntUtils;
import props.Settings;

public class HolesCuts extends Algorithm implements Constants {

	static boolean enabled = Settings.get().getBoolean("gprime.holes.enabled");
	
	static int maxColorCount = Settings.get().getInteger("gprime.holes.maxColorCount");
	static double minColorValue = Settings.get().getDouble("gprime.holes.minColorValue");
	static double maxColorValue = Settings.get().getDouble("gprime.holes.maxColorValue");
	static double maxPerColor = Settings.get().getDouble("gprime.holes.maxPerColor");
	
	IHolesDetector<Node> detector;
	ISimpleGraph graph;
	IPartitionedGraph original;
	
	int colorCount = 0;
	double valueWj;
	int color;
	
	int holeCount;
	
	public HolesCuts(IAlgorithmSource provider) {
		super(provider);
		this.holeCount = 0;
	}
	
	public HolesCuts run() {
		if (!enabled) return this;
		bounder.start();

		for (int c = 0; c < provider.getModel().getColorCount(); c++) {
			final Integer color = c;
			this.color = color;
			valueWj = data.w(color);

			if (valueWj < minColorValue) continue;
			if (valueWj > maxColorValue) continue;
			if (this.colorCount++ > maxColorCount) break;
			
			this.original = provider.getModel().getGraph();
			this.graph = original.getGPrime();
			this.detector = new HolesDetector(graph);
			
			detector.holes(new IHoleHandler<Node>() {
				public boolean handle(final List<Node> hole) {
					if (sum(hole) - Epsilon > IntUtils.floorhalf(hole.size()) * data.w(color)) {
						provider.getCutBuilder().addGPrimeHole(hole, color);
						return (++holeCount <= maxPerColor);
					} return true;
				}

				private double sum(final List<Node> hole) {
					double sum = 0.0;
					for (Node simplenode : hole) {
						for (pcp.entities.partitioned.Node n : original.getNodes(simplenode)) {
							sum += data.x(n.index(), color);
						}
					} return sum;
				}
			}, null);
		}
		
		bounder.stop();
		return this;
	}

	@Override
	public CutFamily getIdentifier() {
		return CutFamily.GPHole;
	}
}
