package pcp.tests.java;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Assert;

import org.junit.Test;


public class JavaTests {

	@Test
	public void testStringFormat() {
		Assert.assertEquals("x[42,93]", 
				String.format("x[%1$d,%2$d]", 42, 93));
	}
	
	@Test
	public void testReverseList() {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		
		ListIterator<String> iterator = list.listIterator(list.size());
		while (iterator.hasPrevious()) {
			String string = iterator.previous();
			sb.append(string);
		}
		
		Assert.assertEquals("dcba", sb.toString());
		
		
	}
	
	
}
