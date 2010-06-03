package pcp;


public class Logger {

	public static void error(String msg, Exception ex) {
		if (ex != null) System.err.println(msg + " " + ex.toString());
		else System.err.println(msg);
		ex.printStackTrace(System.err);
	}
	
}
