package pcp.utils;


public class IntUtils {

	public static int positiveOrZero(int x) {
		if (x < 0) return 0;
		else return x;
	}
	
	public static int negativeOrZero(int x) {
		if (x > 0) return 0;
		else return x;
	}
	
	public static int floorhalf(int x) {
		if (x % 2 == 1) {
			x--;
		} return x / 2;
	}
	
	public static int ceilhalf(int x) {
		if (x % 2 == 1) {
			x++;
		} return x / 2;
	}
	
	public static int minabs(int a, int b) {
		int aa = a < 0 ? -a : a;  
		int bb = b < 0 ? -b : b;
		
		if (aa > bb) return b;
		else return a;
	}
	
	public static Integer max(Integer i1, Integer i2) {
		if (i1 == null) return i2;
		if (i2 == null) return i1;
		if (i1 < i2) return i2;
		return i1;
	}
	
	public static Integer min(Integer i1, Integer i2) {
		if (i1 == null) return i2;
		if (i2 == null) return i1;
		if (i1 < i2) return i1;
		return i2;
	}
	
	
}
