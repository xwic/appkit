/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/

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
