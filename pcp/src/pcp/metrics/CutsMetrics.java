package pcp.metrics;

import ilog.concert.IloRange;

import java.util.LinkedList;

import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.definitions.Cuts;
import props.Settings;

public class CutsMetrics implements Cuts {

	static boolean logIterMetrics = Settings.get().getBoolean("logging.iterMetrics");
	static boolean logTotalMetrics = Settings.get().getBoolean("logging.totalMetrics");
	
	int iter = -1;
	LinkedList<int[]> counts;
	LinkedList<long[]> ticks;
	
	public CutsMetrics() {
		counts = new LinkedList<int[]>();
		ticks = new LinkedList<long[]>();
	}
	
	public void newIter() {
		if (iter >= 0 && logIterMetrics) {
			printIter();
		}
		
		iter++;
		counts.addLast(new int[Cuts]);
		ticks.addLast(new long[Cuts]);
	}
	
	public void added(int cut, IloRange range) {
		this.counts.getLast()[cut]++;
	}
	
	public void iterTime(IBoundedAlgorithm... algorithms) {
		for (IBoundedAlgorithm algorithm : algorithms) {
			this.iterTime(algorithm.getIdentifier(), algorithm.getBounder().getMillis());
		}
	}
	
	public void iterTime(int cut, long ticks) {
		this.ticks.getLast()[cut] += ticks;
	}
	
	public void printIter() {
		System.out.println("Iteration " + iter);
		for (int i = 0; i < Cuts; i++) {
			int c = this.counts.getLast()[i];
			long t = this.ticks.getLast()[i];
			System.out.println(" Generated " + c + " cuts in " + t + " ms of " + Names[i]);
		}
	}
	
	public void printTotal() {
		System.out.println("Total iterations " + (iter + 1));
		for (int i = 0; i < Cuts; i++) {
			int totalc = 0;
			long totalt = 0;
			for (int[] c : counts) {
				totalc += c[i];
			}
			for (long[] t : ticks) {
				totalt += t[i];
			}
			System.out.println(" Generated a total of " + totalc + " cuts in " + totalt + " ms of " + Names[i]);
		}
	}
	
	public int getNIters() {
		return this.counts.size();
	}
	
}
