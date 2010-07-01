package pcp.algorithms.bounding;

import props.Settings;


public class IterationsBounder extends TimeBounder implements IAlgorithmBounder {
	
	int iters;
	int itersWithoutImprovement; 
	
	int maxIters = Integer.MAX_VALUE;
	int maxItersWithoutImprovement = Integer.MAX_VALUE;
	
	
	public IterationsBounder() {
		super();
	}
	
	public IterationsBounder(String settingsPrefix) {
		super(settingsPrefix);
		if (settingsPrefix == null) return;
		
		Settings s = Settings.get();
		maxIters = s.getInteger(settingsPrefix + ".maxIters");
		maxItersWithoutImprovement = s.getInteger(settingsPrefix + ".maxItersWithoutImprovement");
	}
	
	public boolean check() {
		return super.check() && 
			(iters <= maxIters) &&
			(itersWithoutImprovement <= maxItersWithoutImprovement);
	}
	
	public boolean iter() {
		iters++;
		itersWithoutImprovement++;
		return check();
	}
	
	public boolean improved() {
		super.improved();
		itersWithoutImprovement = 0;
		return check();
	}

	
	public int getMaxIters() {
		return maxIters;
	}

	
	public void setMaxIters(int maxIters) {
		this.maxIters = maxIters;
	}

	
	public int getMaxItersWithoutImprovement() {
		return maxItersWithoutImprovement;
	}

	
	public void setMaxItersWithoutImprovement(int maxItersWithoutImprovement) {
		this.maxItersWithoutImprovement = maxItersWithoutImprovement;
	}

	
	public int getIters() {
		return iters;
	}

	
	public int getItersWithoutImprovement() {
		return itersWithoutImprovement;
	}
	
	
}
