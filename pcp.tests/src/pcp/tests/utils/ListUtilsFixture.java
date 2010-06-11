package pcp.tests.utils;

import java.util.Comparator;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import pcp.utils.ListUtils;

public class ListUtilsFixture {

	@Test
	public void testRetainFrom01() {
		LinkedList<Integer> candidates = new LinkedList<Integer>();
		
		candidates.add(2);
		candidates.add(5);
		candidates.add(7);
		
		Integer[] toRetain = new Integer[] {1,2,4,7}; 
		
		LinkedList<Integer> removed = ListUtils.retainFromSorted(candidates, toRetain, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		Assert.assertArrayEquals(new Integer[] { 2,7 }, 
				(Integer[]) candidates.toArray(new Integer[candidates.size()]));
		
		Assert.assertArrayEquals(new Integer[] { 5 }, 
				(Integer[]) removed.toArray(new Integer[removed.size()]));
	}
	
	@Test
	public void testRetainFrom02() {
		LinkedList<Integer> candidates = new LinkedList<Integer>();
		
		candidates.add(2);
		candidates.add(5);
		candidates.add(7);
		
		Integer[] toRetain = new Integer[] {1,2,5,7}; 
		
		LinkedList<Integer> removed = ListUtils.retainFromSorted(candidates, toRetain, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		Assert.assertArrayEquals(new Integer[] { 2,5,7 }, 
				(Integer[]) candidates.toArray(new Integer[candidates.size()]));
		
		Assert.assertArrayEquals(new Integer[] { }, 
				(Integer[]) removed.toArray(new Integer[removed.size()]));
	}
	
	@Test
	public void testAddSorted01() {
		LinkedList<Integer> candidates = new LinkedList<Integer>();
		LinkedList<Integer> toAdd = new LinkedList<Integer>();
		
		candidates.add(2);
		candidates.add(5);
		candidates.add(7);
		
		toAdd.add(1);
		toAdd.add(3);
		toAdd.add(4);
		toAdd.add(9);
		
		ListUtils.addSorted(candidates, toAdd, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		Assert.assertArrayEquals(new Integer[] { 1,2,3,4,5,7,9 }, 
				(Integer[]) candidates.toArray(new Integer[candidates.size()]));
	}
	
	@Test
	public void testAddSorted02() {
		LinkedList<Integer> candidates = new LinkedList<Integer>();
		LinkedList<Integer> toAdd = new LinkedList<Integer>();
		
		candidates.add(4);
		candidates.add(5);
		candidates.add(7);
		
		toAdd.add(1);
		toAdd.add(3);
		
		ListUtils.addSorted(candidates, toAdd, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		Assert.assertArrayEquals(new Integer[] { 1,3,4,5,7 }, 
				(Integer[]) candidates.toArray(new Integer[candidates.size()]));
	}
	
	@Test
	public void testAddSorted03() {
		LinkedList<Integer> candidates = new LinkedList<Integer>();
		LinkedList<Integer> toAdd = new LinkedList<Integer>();
		
		candidates.add(4);
		candidates.add(5);
		candidates.add(7);
		
		toAdd.add(9);
		toAdd.add(11);
		
		ListUtils.addSorted(candidates, toAdd, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		Assert.assertArrayEquals(new Integer[] { 4,5,7,9,11 }, 
				(Integer[]) candidates.toArray(new Integer[candidates.size()]));
	}
	
}
