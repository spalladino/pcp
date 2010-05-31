package pcp.generator.network;

import java.util.List;

import pcp.generator.GeneratorProperties;

public class RingNetworkGraphGenerator extends NetworkGraphGenerator {

	public RingNetworkGraphGenerator(GeneratorProperties gp) {
		super(gp);
	}

	@Override
	protected List<Lightpath> getPaths() {
		RingNetworkPathsGenerator generator = new RingNetworkPathsGenerator(gp.getNodeCount());
		RequestsMatrix matrix = new RequestsMatrix(gp.getNodeCount()).populateRandom(gp.getRequests());
		return generator.generate(matrix);
	}

}
