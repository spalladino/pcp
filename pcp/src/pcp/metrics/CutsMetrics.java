package pcp.metrics;

import ilog.concert.IloRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pcp.algorithms.bounding.IBoundedAlgorithm;
import pcp.solver.cuts.CutFamily;
import props.Settings;

public class CutsMetrics {

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
		counts.addLast(new int[CutFamily.values().length]);
		ticks.addLast(new long[CutFamily.values().length]);
	}
	
	public void added(CutFamily cut, IloRange range) {
		this.counts.getLast()[cut.ordinal()]++;
	}
	
	public void iterTime(IBoundedAlgorithm... algorithms) {
		for (IBoundedAlgorithm algorithm : algorithms) {
			this.iterTime(algorithm.getIdentifier().ordinal(), algorithm.getBounder().getMillis());
		}
	}
	
	public void iterTime(int cut, long ticks) {
		this.ticks.getLast()[cut] += ticks;
	}
	
	public void printIter() {
		System.out.println("Iteration " + iter);
		for (int i = 0; i < CutFamily.count(); i++) {
			int c = this.counts.getLast()[i];
			long t = this.ticks.getLast()[i];
			System.out.println(" Generated " + c + " cuts in " + t + " ms of " + CutFamily.values()[i].toString());
		}
	}
	
	public void printTotal() {
		System.out.println("Total iterations " + (iter + 1));
		for (int i = 0; i < CutFamily.count(); i++) {
			int totalc = 0;
			long totalt = 0;
			for (int[] c : counts) {
				totalc += c[i];
			}
			for (long[] t : ticks) {
				totalt += t[i];
			}
			System.out.println(" Generated a total of " + totalc + " cuts in " + totalt + " ms of " + CutFamily.getName(i));
		}
	}
	
	public int getNIters() {
		return this.counts.size();
	}

	public void fillData(Map<String, Object> data) {
		Map<String, Object> cuts;
		data.put("cuts", cuts = new HashMap<String, Object>());
		data.put("cuts.niters", getNIters());
		
		for (int i = 0; i < CutFamily.count(); i++) {
			Map<String, Object> cut;
			cuts.put(CutFamily.getName(i), cut = new HashMap<String, Object>());
			
			int totalc = 0;
			long totalt = 0;
			
			List<Map<String, Object>> iters = new ArrayList<Map<String,Object>>();
			for (int j = 0; j < counts.size(); j++) {
				Map<String, Object> iter;
				iters.add(iter = new HashMap<String, Object>());
				
				int count = counts.get(j)[i];
				long tick = ticks.get(j)[i];
				
				iter.put("count", count);
				iter.put("ticks", tick);
				
				totalc += count;
				totalt += tick;
			}
			
			cut.put("iters", iters);
			cut.put("count", totalc);
			cut.put("ticks", totalt);
		}
	}
	
}
