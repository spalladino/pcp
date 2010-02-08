package pcp.tests.utils;

import junit.framework.Assert;

import org.junit.Test;

import pcp.utils.IntUtils;

public class IntUtilsFixture {
	
	@Test
	public void testFloorhalfOdd() {
		Assert.assertEquals(2, IntUtils.floorhalf(5));
	}

	@Test
	public void testFloorhalfEven() {
		Assert.assertEquals(3, IntUtils.floorhalf(6));
	}
}
