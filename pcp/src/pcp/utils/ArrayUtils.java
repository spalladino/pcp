package pcp.utils;

public class ArrayUtils {

	public static boolean areEqual(Object[] a1, Object[] a2) {
		if (a1.length != a2.length) return false;
		for (int i = 0; i < a2.length; i++) {
			if (!a1[i].equals(a2[i])) {
				return false;
			}
		} return true;
	}
	
	/**
	 * Checks whether the first array contains all elements of the second.
	 * Both arrays must be sorted.
	 * @param container the bigger array
	 * @param contained the smaller array
	 * @return true iif the first array contains all elements of the second.
	 */
	public static boolean containsSorted(Object[] container, Object[] contained) {
		if (container.length < contained.length) return false;
		int j = 0;
		
		for (int i = 0; i < container.length; i++) {
			if (j == contained.length) return true;
			if (container[i].equals(contained[j])) j++;
		}
		
		return (j == contained.length);
	}
	
	public static <T> T[] concat(T[] a, T[] b, T[] target) {
		   T[] c = target;
		   System.arraycopy(a, 0, c, 0, a.length);
		   System.arraycopy(b, 0, c, a.length, b.length);
		   return c;
		}
}
