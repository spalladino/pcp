package pcp.algorithms.bounding;

import props.Settings;

public class SolutionsBounder extends IterationsBounder {

	int maxSolutions;
	int solutions;
	
	public SolutionsBounder() {
		super();
	}
	
	public SolutionsBounder(String settingsPrefix) {
		super(settingsPrefix);
		if (settingsPrefix == null) return;
		
		Settings s = Settings.get();
		maxSolutions = s.getInteger(settingsPrefix + ".maxSolutions");
	}

	
	public int getMaxSolutions() {
		return maxSolutions;
	}

	public void setMaxSolutions(int maxSolutions) {
		this.maxSolutions = maxSolutions;
	}

	public int getSolutions() {
		return solutions;
	}

	public void setSolutions(int solutions) {
		this.solutions = solutions;
	}

	@Override
	public boolean improved() {
		solutions++;
		return super.improved();
	}
	
	@Override
	public boolean check() {
		return super.check() &&
			(solutions < maxSolutions);
	}
	
}
