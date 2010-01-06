package pcp.tests.utils;

import org.junit.Test;

import junit.framework.Assert;

import static pcp.utils.DoubleUtils.*;

public class DoubleUtilsFixture {
	
	@Test
	public void testEquals() {
		Assert.assertTrue(doubleEquals(5.0, 5.0));
	}
	
	@Test
	public void testEqualsLT() {
		Assert.assertTrue(doubleEquals(4.9999999, 5.0));
	}
	
	@Test
	public void testEqualsGT() {
		Assert.assertTrue(doubleEquals(5.0, 5.0000001));
	}
	
	@Test
	public void testEqualsNeg() {
		Assert.assertTrue(doubleEquals(-5.0, -5.0));
	}
	
	@Test
	public void testEqualsNegGT() {
		Assert.assertTrue(doubleEquals(-5.0, -4.9999999));
	}
	
	@Test
	public void testEqualsNegLT() {
		Assert.assertTrue(doubleEquals(-5.00000001, -5.0));
	}
	
	@Test
	public void testNotEquals() {
		Assert.assertFalse(doubleEquals(5.1, 5.0));
	}
	
	@Test
	public void testNotEqualsNeg() {
		Assert.assertFalse(doubleEquals(-5.1, -5.0));
	}

	
}
