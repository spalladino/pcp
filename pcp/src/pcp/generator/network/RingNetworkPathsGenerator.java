package pcp.generator.network;

import java.util.ArrayList;
import java.util.List;

import pcp.generator.network.Lightpath.Route;

public class RingNetworkPathsGenerator implements IPathsGenerator {

	int nodeCount;
	
	public RingNetworkPathsGenerator(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	
	@Override
	public List<Lightpath> generate(RequestsMatrix requests) {
		List<Lightpath> paths = new ArrayList<Lightpath>();
		
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j && requests.hasRequest(i, j)) {
					Lightpath path = new Lightpath();
					path.withRoute(createClockwiseRoute(i,j));
					path.withRoute(createCounterClockwiseRoute(i,j));
					paths.add(path);
				}
			}	
		} 
		
		return paths;
	}

	private Route createClockwiseRoute(int from, int to) {
		Route path = new Route();
		if (to < from) to += nodeCount;
		for (int i = from; i <= to; i++) {
			path.getNodes().add(i % nodeCount);
		} return path;
	}

	private Route createCounterClockwiseRoute(int from, int to) {
		Route path = new Route();
		if (to > from) from += nodeCount;
		for (int i = from; i >= to; i--) {
			path.getNodes().add(i % nodeCount);
		} return path;
	}

}
