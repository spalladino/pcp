package pcp.algorithms.bounding;


public interface IAlgorithmBounder extends IAlgorithmData {

	boolean check();
		
	void start();
	
	void end();
	
	void stop();
}
