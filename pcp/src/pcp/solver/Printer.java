package pcp.solver;

import ilog.concert.IloException;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import pcp.interfaces.ISolutionData;


public class Printer {

	ISolutionData solver;
	PrintStream out = System.out;
	
	public Printer(ISolutionData solver) {
		this.solver = solver;
	}
	
	public void printSolution(boolean verbose) throws IloException {
		if (verbose) {
			printVerboseSolution();
		} else {
			printShortSolution();
		}
	}

	public void printVerboseSolution() throws IloException {
		out.println("Solution values:");
		out.println("W: " + Arrays.toString(solver.ws()));
		for (int n = 0; n < solver.getGraph().getNodes().length; n++) {
			out.println("X[" + n + "]: " + Arrays.toString(solver.xs(n)));
		}
		out.println();
	}

	public void printShortSolution() throws IloException {
		StringBuilder builder = new StringBuilder();
		List<Integer> colorsUsed = solver.getColorsUsed();
		String colors = Arrays.toString((Integer[]) colorsUsed.toArray(new Integer[colorsUsed.size()]));
		builder.append("Colors: ").append(colors).append("\n");
		for (int n = 0; n < solver.getGraph().getNodes().length; n++) {
			int color = 0;
			if (-1 != (color = solver.getNodeColor(n))) {
				builder.append("X[").append(n).append("]: ").append(color).append("\n");
			}
		}
		out.print(builder.toString());
	}
}
