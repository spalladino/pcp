package pcp.generator.network;

import java.util.ArrayList;
import java.util.List;

import pcp.utils.IntUtils;

public class Lightpath {

	public static class UndirectedLink extends Link {
		public UndirectedLink(int from, int to) {
			super(IntUtils.min(from, to), IntUtils.max(from, to));
		}
	}
	
	public static class Link {
		int from;
		int to;
		
		public Link(int from, int to) {
			this.from = from;
			this.to = to;
		}

		public int getFrom() {
			return from;
		}

		public int getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + from;
			result = prime * result + to;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Link other = (Link) obj;
			if (from != other.from)
				return false;
			if (to != other.to)
				return false;
			return true;
		}
	}
	
	public static class Route {

		private List<Integer> nodes;

		public Route() {
			this.nodes = new ArrayList<Integer>();
		}
		
		public List<Link> getDirectedLinks() {
			List<Link> links = new ArrayList<Link>(this.nodes.size()-1);
			for (int i = 1; i < this.nodes.size(); i++) {
				links.add(new Link(nodes.get(i-1), nodes.get(i)));
			} return links;
		}
		
		public List<UndirectedLink> getUndirectedLinks() {
			List<UndirectedLink> links = new ArrayList<UndirectedLink>(this.nodes.size()-1);
			for (int i = 1; i < this.nodes.size(); i++) {
				links.add(new UndirectedLink(nodes.get(i-1), nodes.get(i)));
			} return links;
		}
		
		public List<Integer> getNodes() {
			return nodes;
		}
		
		public Route withNodes(Integer... nodes){
			for (Integer node : nodes) {
				this.nodes.add(node);
			} return this;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Route other = (Route) obj;
			if (nodes == null) {
				if (other.nodes != null)
					return false;
			} else if (!nodesMatch(other.getNodes())) 
				return false;
			return true;
		}

		private boolean nodesMatch(List<Integer> other) {
			for (int i = 0; i < this.nodes.size(); i++) {
				if (!other.get(i).equals(nodes.get(i))) {
					return false;
				}
			} return true;
		}
		
	}		
	
	private List<Route> routes;
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Lightpath() {
		this.routes = new ArrayList<Route>();
	}
	
	public Lightpath withRoute(Route route) {
		this.routes.add(route);
		return this;
	}
	
	public List<Route> getRoutes() {
		return this.routes;
	}
	
}
