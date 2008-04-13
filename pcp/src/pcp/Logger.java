package pcp;


public class Logger {

	public static void error(String msg, Exception ex) {
		System.err.println(msg + " " + ex.toString());
		ex.printStackTrace(System.err);
	}
	
}
