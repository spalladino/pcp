package pcp.algorithms.bounding;


public class Bounder implements IAlgorithmBounder {

	private boolean stopped = false;
	private int iters = 0; 
	private int partials = 0;
	private long init = 0;
	private long end = 0;
	
	private int maxIters = Integer.MAX_VALUE;
	private int maxPartials = Integer.MAX_VALUE;
	private long maxTime = Long.MAX_VALUE;
	
	@Override
	public boolean check() {
		return !stopped
			&& iters < maxIters
			&& partials < maxPartials
			&& end - init < maxTime;
	}

	@Override
	public int getIters() {
		return iters;
	}

	@Override
	public boolean incIters() {
		iters++;
		return check();
	}

	@Override
	public void end() {
		end = System.currentTimeMillis();
	}

	@Override
	public void start() {
		init = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		stopped = true;
		end();
	}

	@Override
	public long getMillis() {
		return end - init;
	}

	@Override
	public boolean incPartials() {
		partials++;
		return check();
	}

	@Override
	public int getPartials() {
		return partials;
	}

	
	public int getMaxIters() {
		return maxIters;
	}

	
	public void setMaxIters(int maxIters) {
		this.maxIters = maxIters;
	}

	
	public int getMaxPartials() {
		return maxPartials;
	}

	
	public void setMaxPartials(int maxPartials) {
		this.maxPartials = maxPartials;
	}

	
	public long getMaxTime() {
		return maxTime;
	}

	
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
	
	
	
}
