package pcp.generator.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcp.generator.DimacsPartitionedGraph;
import pcp.generator.GraphProperties;
import pcp.generator.network.Lightpath.UndirectedLink;
import pcp.generator.network.Lightpath.Route;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class NetworkGraphBuilder {

	private GraphProperties gp;

	public NetworkGraphBuilder(GraphProperties gp) {
		this.gp = gp;
	}
	
	public DimacsPartitionedGraph buildGraph(List<Lightpath> paths) {
		DimacsPartitionedGraph graph = new DimacsPartitionedGraph(gp);
		
		// Extract network graph data
		Map<Route, Integer> routeIds = getRouteIds(paths);
		Multimap<UndirectedLink, Route> linksRoutes = getLinksRoutes(paths);
		
		// Each route is represented by a node
		int nodeCount = routeIds.size();  
		graph.setNodes(nodeCount);

		// Every lightpath is a partition
		for (Lightpath path : paths) {
			List<Integer> nodes = new ArrayList<Integer>();
			for (Route route : path.getRoutes()) {
				nodes.add(routeIds.get(route));
			} graph.addPartition(nodes);
		}
		
		// Conflicts arise when two routes share a link
		boolean[][] conflicts = new boolean[nodeCount][nodeCount];
		for (UndirectedLink link : linksRoutes.keySet()) {
			for (Route r1 : linksRoutes.get(link)) {
				for (Route r2 : linksRoutes.get(link)) {
					if (r1 != r2) {
						conflicts[routeIds.get(r1)][routeIds.get(r2)] = true;
						conflicts[routeIds.get(r2)][routeIds.get(r1)] = true;
					}
				}	
			}
		}
		
		// Conflicts between routes are represented as edges between nodes
		for (int i = 0; i < nodeCount; i++) {
			for (int j = i+1; j < nodeCount; j++) {
				if (conflicts[i][j]) {
					graph.addEdge(i, j);
				}
			}
		}
		
		return graph;
	}

	private Map<Route, Integer> getRouteIds(List<Lightpath> paths) {
		Map<Route, Integer> routeIds = new HashMap<Route, Integer>();
		int nodeId = 0;
		for (Lightpath path : paths) {
			for (Route route : path.getRoutes()) {
				if (!routeIds.containsKey(route)) {
					routeIds.put(route, nodeId++);
				}
			}
		}
		return routeIds;
	}
	
	private Multimap<UndirectedLink, Route> getLinksRoutes(List<Lightpath> paths) {
		Multimap<UndirectedLink, Route> linksRoutes = HashMultimap.create();
		for (Lightpath path : paths) {
			for (Route route : path.getRoutes()) {
				for (UndirectedLink link : route.getUndirectedLinks()) {
					linksRoutes.put(link, route);
				}
			}
		} return linksRoutes;
	}
	

	
}
