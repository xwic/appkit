/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.util;

import junit.framework.TestCase;

/**
 * @author lippisch
 */
public class EqualsTest extends TestCase {

	public void testEqualText() {
		
		assertTrue(Equals.equalText(null, null));
		assertFalse(Equals.equalText("abc", null));
		assertFalse(Equals.equalText(null, "abc"));
		
		assertFalse(Equals.equalText("abc", "123"));
		assertTrue(Equals.equalText("abc", "abc"));
		assertTrue(Equals.equalText("abc", "ABC"));
		assertTrue(Equals.equalText("a b c ", "abc"));
		assertTrue(Equals.equalText("abc", "a b c "));
		assertTrue(Equals.equalText("Abc", "ab     \nc    "));

		assertFalse(Equals.equalText("abcd", "abc    "));
		assertFalse(Equals.equalText("abc", "abcd"));
		
	}
	
}
