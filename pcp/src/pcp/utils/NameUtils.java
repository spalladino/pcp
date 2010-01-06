package pcp.utils;


public class NameUtils {

	public static String plus(int val) {
		if (val == 0) return "";
		else if (val > 0) return "+" + String.valueOf(val);
		else return String.valueOf(val);
	}
	
	public static String asTerm(int coef, int i, int j) {
		return asCoef(coef) + asVar(i, j);
	}

	public static String asTerm(int coef, int j) {
		return asCoef(coef) + asVar(j);
	}
	
	public static String asTerm(int coef) {
		if (coef >= 0) return "+" + String.valueOf(coef);
		else return String.valueOf(coef);
	}

	public static String asCoef(int coef) {
		String c = "+0";
		if (coef == -1) c = "-";
		else if (coef < 0) c = String.valueOf(coef);
		else if (coef == 1) c = "+";
		else if (coef > 0) c = "+" + String.valueOf(coef);
		return c;
	}
	
	public static String asVar(String i, String j) {
		return String.format("x[%1$s,%2$s]", i, j);
	}
	
	public static String asVar(String j) {
		return String.format("w[%1$s]", j);
	}
	
	public static String asVar(int i, int j) {
		return String.format("x[%1$d,%2$d]", i, j);
	}
	
	public static String asVar(int j) {
		return String.format("w[%1$d]", j);
	}
	
	public static String asCmp(int compare) {
		String cmp = "==";
		if (compare < 0) cmp = "<=";
		else if (compare > 0) cmp = ">=";
		return cmp;
	}
}
