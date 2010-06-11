package pcp.utils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;


public class ListUtils {
	
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
