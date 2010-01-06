package pcp.tests.utils;

import org.junit.Test;

import junit.framework.Assert;
import static pcp.utils.ArrayUtils.*;


public class ArrayUtilsFixture {
	
	@Test
	public void testContainsEdgesMatch() {
		Integer[] container= new Integer[] { 1,2,4,7,8,9 };
		Integer[] contained= new Integer[] { 1,2,4,9 };
		Assert.assertTrue(containsSorted(container, contained));
	}
	
	@Test
	public void testContainsEdgesDontMatch() {
		Integer[] container= new Integer[] { 1,2,4,7,8,9 };
		Integer[] contained= new Integer[] { 2,4,8 };
		Assert.assertTrue(containsSorted(container, contained));
	}
	
	@Test
	public void testContainsEqualArrays() {
		Integer[] container= new Integer[] { 1,2,4,7,8,9 };
		Integer[] contained= new Integer[] { 1,2,4,7,8,9 };
		Assert.assertTrue(containsSorted(container, contained));
	}
	
	@Test
	public void testNotContainsWrongLength() {
		Integer[] container= new Integer[] { 1,2,4,7,8,9 };
		Integer[] contained= new Integer[] { 1,2,4,6,7,8,9 };
		Assert.assertFalse(containsSorted(container, contained));
	}
	
	@Test
	public void testNotContainsElements() {
		Integer[] container= new Integer[] { 1,2,4,7,8,9 };
		Integer[] contained= new Integer[] { 4,6,7,8,9 };
		Assert.assertFalse(containsSorted(container, contained));
	}
	
}
