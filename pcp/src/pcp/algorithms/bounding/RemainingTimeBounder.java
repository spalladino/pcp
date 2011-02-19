package pcp.algorithms.bounding;

import pcp.interfaces.ITimeProvider;

public class RemainingTimeBounder implements IAlgorithmBounder {

	private ITimeProvider time;
	private double endTime;

	private boolean stopped = false;
	private double init = 0;
	private double end = 0;
	private double lastImproved = 0;
	
	public RemainingTimeBounder(ITimeProvider time, double endTime) {
		this.time = time;
		this.endTime = endTime;
	}
	
	@Override
	public boolean check() {
		return !stopped && (time.getTime() < endTime);
	}

	@Override
	public void end() {
		end = time.getTime();
	}

	@Override
	public long getLastImproved() {
		if (lastImproved == 0) return 0L;
		return (long) (lastImproved - init);
	}

	@Override
	public boolean improved() {
		lastImproved = time.getTime();
		return check();
	}

	@Override
	public boolean iter() {
		return check();
	}

	@Override
	public void start() {
		init = time.getTime();
	}

	@Override
	public void stop() {
		stopped = true;
		end();
	}

	@Override
	public long getMillis() {
		return (long) (end - init);
	}

	
	
}
