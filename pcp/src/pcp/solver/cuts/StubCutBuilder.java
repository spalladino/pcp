package pcp.solver.cuts;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pcp.common.Tuple;
import pcp.entities.Node;
import pcp.interfaces.ICutBuilder;


public class StubCutBuilder implements ICutBuilder {

	List<Tuple<Integer, List<Node>>> cliques = new ArrayList<Tuple<Integer,List<Node>>>();
	
	@Override
	public void addClique(List<Node> nodes, int color) {
		cliques.add(new Tuple<Integer, List<Node>>(color, nodes));
	}

	public List<Tuple<Integer, List<Node>>> getCliques() {
		return cliques;
	}

	public void printIneqs(PrintStream stream) {
		stream.print("Clique:\n");
		for (Tuple<Integer, List<Node>> t : cliques) {
			stream.print(' ');
			stream.print(t);
			stream.print('\n');
		}
	}
	
}
