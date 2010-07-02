package pcp.algorithms.bounding;


public interface IAlgorithmBounder extends IAlgorithmData {

	boolean check();
	
	boolean iter();
	
	boolean improved();
	
	void start();
	
	void end();
	
	void stop();
	
	long getLastImproved();
}
