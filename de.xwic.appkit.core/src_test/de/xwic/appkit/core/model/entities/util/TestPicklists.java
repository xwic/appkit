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
/**
 *
 */
package de.xwic.appkit.core.model.entities.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Alexandru Bledea
 * @since Oct 13, 2014
 */
@SuppressWarnings("static-method")
public class TestPicklists {

	private static final String KEY1 = "key1";
	private static final String KEY2 = "key2";
	private static final String KEY3 = "key3";
	private static final String KEY4 = "key4";

	private static final String LANG1 = "lang1";
	private static final String LANG2 = "lang2";

	/**
	 *
	 */
	@Test
	public void testGetKeys() {
		assertEquals(null, Picklists.getKey(null));

		final IPicklistEntry key3 = createWithKey(KEY3);
		assertEquals(KEY3, Picklists.getKey(key3));

		final IPicklistEntry key1 = createWithKey(KEY1);
		final IPicklistEntry key2 = createWithKey(KEY2);
		final List<IPicklistEntry> entries = Arrays.asList(key1, key2, key3);
		assertEquals(Arrays.asList(KEY1, KEY2, KEY3), Picklists.getKeys(entries));

		assertFalse(Picklists.containsKey(entries, KEY4));
		assertTrue(Picklists.containsKey(entries, KEY3));

		assertFalse(Picklists.isPicklistKey(key3, KEY4));
		assertTrue(Picklists.isPicklistKey(key3, KEY3));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testStrangeKeys() throws Exception {
		final IPicklistEntry plNull = createWithKey(null);
		assertTrue(Picklists.isPicklistKey(plNull, null));
		assertFalse(Picklists.isPicklistKey(plNull, ""));

		final IPicklistEntry horse = createWithKey("horse");
		assertFalse(Picklists.isPicklistKey(horse, ""));
		assertFalse(Picklists.isPicklistKey(horse, null));
		assertFalse(Picklists.isPicklistKey(null, null));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetTextEn() throws Exception {
		final IPicklistEntry withNull = createWithText(null);
		assertEquals("", Picklists.getTextEn(withNull));

		final IPicklistEntry lang1 = createWithText(LANG1);
		assertEquals(LANG1, Picklists.getTextEn(lang1));

		final IPicklistEntry lang2 = createWithText(LANG2);
		final List<IPicklistEntry> langs = Arrays.asList(lang1, lang2);
		assertEquals(Arrays.asList(LANG1, LANG2), Picklists.getTextEn(langs));
	}

	/**
	 * @param key
	 * @return
	 */
	private static IPicklistEntry createWithKey(final String key) {
		final IPicklistEntry mock = Mockito.mock(IPicklistEntry.class);
		Mockito.when(mock.getKey()).thenReturn(key);
		return mock;
	}

	/**
	 * @param key
	 * @return
	 */
	private static IPicklistEntry createWithText(final String text) {
		final IPicklistEntry mock = Mockito.mock(IPicklistEntry.class);
		Mockito.when(mock.getBezeichnung(Locale.ENGLISH.getLanguage())).thenReturn(text);
		return mock;
	}

}
