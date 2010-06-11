package pcp.utils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import pcp.common.iterate.ArrayIterator;


public class ListUtils {
	
	public static <T> LinkedList<T> retainFromSorted(LinkedList<T> nodes, T[] nodesToRetain, Comparator<T> c) {
		ListIterator<T> it = nodes.listIterator();
		LinkedList<T> removed = new LinkedList<T>();
		ArrayIterator<T> itRetain = new ArrayIterator<T>(nodesToRetain);
		T retainCurrent = null;
		
		while(it.hasNext()) {
			T node = it.next();
			while((retainCurrent == null || c.compare(retainCurrent, node) < 0) && itRetain.hasNext()) {
				retainCurrent = itRetain.next();
			}
			
			if (retainCurrent == null || !retainCurrent.equals(node)) {
				removed.add(node);
				it.remove();
			}
		}
	
		return removed;
	}

	
	public static <T> void addSorted(LinkedList<T> list, LinkedList<T> toadd, Comparator<T> c) {
		ListIterator<T> it = list.listIterator();
		ListIterator<T> itToAdd = toadd.listIterator();
		
		while(itToAdd.hasNext()) {
			T nodeToAdd = itToAdd.next();
			T candidate = it.hasNext() ? it.next() : null;
			
			while((candidate != null && c.compare(candidate, nodeToAdd) < 0)) {
				candidate = it.hasNext() ? it.next() : null;
			} 
			
			if (candidate != null) it.previous(); 
			it.add(nodeToAdd);
		}
	}

	
}
