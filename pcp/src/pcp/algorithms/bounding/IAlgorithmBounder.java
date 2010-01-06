package pcp.algorithms.bounding;


public interface IAlgorithmBounder extends IAlgorithmData {

	boolean check();
	
	boolean incIters();

	boolean incPartials();
	
	void start();
	
	void end();
	
	void stop();
}
