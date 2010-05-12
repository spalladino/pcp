package pcp.algorithms.bounding;

import props.Settings;


public class TimeBounder implements IAlgorithmBounder {

	private boolean stopped = false;
	private long init = 0;
	private long end = 0;
	
	private long maxTime = Long.MAX_VALUE;
	
	public TimeBounder() {
	}
	
	public TimeBounder(String settingsPrefix) {
		if (settingsPrefix == null) return;
		Settings s = Settings.get();
		if (s.hasSetting(settingsPrefix + ".maxTime")) 
			maxTime = s.getInteger(settingsPrefix + ".maxTime");
	}
	
	@Override
	public boolean check() {
		return !stopped
			&& System.currentTimeMillis() - init < maxTime;
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

	@Override
	public boolean improved() {
		return check();
	}

	@Override
	public boolean iter() {
		return check();
	}
	
	
	
}
