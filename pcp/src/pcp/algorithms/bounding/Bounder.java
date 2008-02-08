package pcp.algorithms.bounding;


public class Bounder implements IAlgorithmBounder {

	private boolean stopped = false;
	private long init = 0;
	private long end = 0;
	
	private long maxTime = Long.MAX_VALUE;
	
	@Override
	public boolean check() {
		return !stopped
			&& end - init < maxTime;
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

	public long getMaxTime() {
		return maxTime;
	}

	
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
	
	
	
}
