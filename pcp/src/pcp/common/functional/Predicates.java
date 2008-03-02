package pcp.common.functional;

import java.util.Map;

import com.google.common.base.Predicate;


public class Predicates {

	public static <T> Predicate<T> inKeys(final Map<T,?> map) {
		return new InKeysPredicate<T>(map);
	}
	
	private static class InKeysPredicate<T> implements Predicate<T> {
		private Map<T, ?> map;

		public InKeysPredicate(Map<T,?> map) {
			this.map = map;
		}
		
		@Override
		public boolean apply(T arg0) {
			return map.containsKey(arg0);
		}
	}
	
}
