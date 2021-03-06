package pcp.algorithms.holes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pcp.entities.ISimpleGraph;
import pcp.entities.simple.Edge;
import pcp.entities.simple.Node;
import pcp.utils.SimpleGraphUtils;
import props.Settings;

public class HolesDetector implements IHolesDetector<Node> {

	static boolean check = Settings.get().getBoolean("validate.holes");
	static boolean storeVisited = Settings.get().getBoolean("gprime.holes.storeVisited");
	
	static boolean printHoles = false;
	
    private ISimpleGraph graph;

    private boolean[][][] notInHole;
    private int[] inPath;
    private List<Node> pathVertex;
    
    private boolean[] returned;
    private Set<List<Node>> visited;
    
    public HolesDetector(ISimpleGraph g) {
        this.graph = g;
    }
    
    public void holes(IHoleHandler<Node> handler, IHoleFilter<Node> filter) {
        int n = graph.getNodes().length;    
    	
        notInHole = new boolean[n][n][n];
        inPath = new int[n];
        pathVertex = new ArrayList<Node>();
        returned = new boolean[n];
        visited = new HashSet<List<Node>>();
      
        for (Node node : graph.getNodes()) {
            int u = node.index();
        	if (returned[u]) continue;

            for(Edge e : graph.getEdges()) {
                Node ev1 = e.getNode1();
                Node ev2 = e.getNode2();
            	
                if (!processEdge(handler, filter, node, u, ev1, ev2)) return;
                //if (!processEdge(handler, filter, node, u, ev2, ev1)) return;
            }

            inPath[u] = 0;
        }
    }

	private boolean processEdge(IHoleHandler<Node> handler, IHoleFilter<Node> filter, Node node, int u, Node ev1, Node ev2) {
		
		if (node != ev1 && node != ev2
		    && graph.areAdjacent(node, ev1)
		    && !graph.areAdjacent(node, ev2)
		    && !notInHole[node.index()][ev1.index()][ev2.index()])
		{
			inPath = new int[graph.getNodes().length];
		    inPath[u] = 1;
		    inPath[ev1.index()] = 2;

		    pathVertex.clear();
		    pathVertex.add(node);
		    pathVertex.add(ev1);
		    pathVertex.add(ev2);

		    ProcessResult result = process(node, ev1, ev2, 3);
		    
		    if (result.success)
		    {
		        List<Node> hole = getHole(inPath[result.nodeE.index()], result.k);

		        if (check && !checkHole(hole)) {
        			return false;
	        	}
		        
		        if (storeVisited) {
		        	List<Node> sorted = new ArrayList<Node>(hole);
		        	Collections.sort(sorted);
		        	if (visited.contains(sorted)) {
		        		return true;
		        	} visited.add(sorted);
		        }
		        
	        	if (filter == null || filter.use(hole)) { 
		            if (!handler.handle(hole)) {
		            	return false;
		            }
		        }
		            
		        for (Node x : hole) {
	                returned[x.index()] = true;
	            }
		    
		        for (Node x : pathVertex) {
		            inPath[x.index()] = 0;
		        }

		    }

		    inPath[ev1.index()] = 0;
		}
		
		return true;
	}

    private ProcessResult process(Node aa, Node bb, Node cc, int i) {
    	int a = aa.index(), b = bb.index(), c = cc.index();
    	
    	inPath[c] = i;
        for(Node dd : graph.getNeighbours(cc)) {
            int d = dd.index();
        	
            if (d != a 
    			&& d != b 
    			&& d != c 
    			&& !graph.areAdjacent(aa, dd) 
    			&& !graph.areAdjacent(bb, dd)) 
        	{
                pathVertex.add(dd);
                
                if (inPath[d] != 0) {
                	return new ProcessResult(true, dd, i);
                } else if (!notInHole[b][c][d]) {
                	ProcessResult result = process(bb, cc, dd, i + 1); 
                	if (result.success) {
                        return result;
                    }
                }
                
                pathVertex.remove(pathVertex.size() - 1);
            }
        }

        inPath[c] = 0;
        notInHole[a][b][c] = true;
        notInHole[c][b][a] = true;

        return new ProcessResult();
    }

    private List<Node> getHole(int j, int k) {
        int imin = j;
        int imax = k;
        int i = imin;
        int h;
        Node uu;

        do {
            uu = pathVertex.get(i-1);
        	h = imax + 1;
            for (Node xx : graph.getNeighbours(uu)) {
            	int x = xx.index();
                if (inPath[x] >= i + 4 && inPath[x] < h) {
                    h = inPath[x];
                }
            }
            if (h <= imax) {
                imin = i;
                imax = h;
            }
            i++;
        } while (!(i > imax - 4));

        return pathVertex.subList(imin-1, imax);
    }

    private String listHole(List<Node> hole) {
        StringBuilder builder = new StringBuilder("[");
        for (Node h : hole) {
            builder.append(h.index()).append(',');
        } builder.append("]");
        return builder.toString();
    }

    public boolean checkHole(List<Node> hole) {
        
    	if (!SimpleGraphUtils.checkHole(graph, hole)) {
    		return false;
    	}
    	
        if (printHoles) {
        	System.out.println(listHole(hole));
        }
        
        return true;
    }

	private static class ProcessResult
	{
		public ProcessResult() {
			this.success = false;
		}
		
		public ProcessResult(boolean success, Node nodeE, int k) {
			this.success = success;
			this.nodeE = nodeE;
			this.k = k;
		}
		
		public boolean success;
		public Node nodeE;
		public int k;
	}


}
	
