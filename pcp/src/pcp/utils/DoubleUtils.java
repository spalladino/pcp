package pcp.utils;


public class DoubleUtils {
	
	public static final double eps = 1.0e-6;
	
	public static boolean doubleEquals(double d1, double d2) {
		return Math.abs(d1 - d2) < eps;
	}
	
	public static double fractionality(double d) {
		if (d > 0.5) return 1.0 - d;
		else return d;
	}
	
	public static boolean isTrue(double d) {
		return doubleEquals(d, 1.0);
	}
	
	public static int ceil(double d) {
		return (int) Math.ceil(d - eps);
	}
	
	public static double sum(double[] ds) {
		double sum = 0.0;
		for (double d : ds) {
			sum += d;
		} return sum;
	}
	
}
