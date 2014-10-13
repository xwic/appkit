/**
 *
 */
package de.xwic.appkit.core.model.entities.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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

	/**
	 *
	 */
	@Test
	public void testGetKeys() {
		assertEquals(null, Picklists.getKey(null));

		final IPicklistEntry key3 = createEntry(KEY3);
		assertEquals(KEY3, Picklists.getKey(key3));

		final IPicklistEntry key1 = createEntry(KEY1);
		final IPicklistEntry key2 = createEntry(KEY2);
		final List<IPicklistEntry> entries = Arrays.asList(key1, key2, key3);
		assertEquals(Arrays.asList(KEY1, KEY2, KEY3), Picklists.getKeys(entries));

		assertFalse(Picklists.containsKey(entries, KEY4));
		assertTrue(Picklists.containsKey(entries, KEY3));

	}

	/**
	 * @param key
	 * @return
	 */
	private static IPicklistEntry createEntry(final String key) {
		final IPicklistEntry mock = Mockito.mock(IPicklistEntry.class);
		Mockito.when(mock.getKey()).thenReturn(key);
		return mock;
	}
}
