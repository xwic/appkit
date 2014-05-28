/**
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Alexandru Bledea
 * @since May 28, 2014
 */
public class PropertyQueryTest {

	private static final IEntityQueryResolver resolver = new PropertyQueryResolver();

	/**
	 * 
	 */
	@Test
	public void testSimpleQuery() {
		final PropertyQuery pq = new PropertyQuery();
		final String onlyQuery = onlyQuery(IMitarbeiter.class, pq);
		final String expectedQuery = " FROM de.xwic.appkit.core.model.entities.IMitarbeiter AS obj \n" +
				" WHERE  obj.deleted = 0  ";
		assertEquals(expectedQuery, onlyQuery);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNullQuery() {
		onlyQuery(IEntity.class, null);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testWrongQuery() {
		onlyQuery(IPicklistEntry.class, new EntityQuery() {
		});
	}

	/**
	 * @param clasz
	 * @param query
	 * @return
	 */
	@SuppressWarnings ({"rawtypes", "unchecked"})
	private static String onlyQuery(final Class<? extends Object> clasz, final EntityQuery query) {
		final List list = new ArrayList();
		return resolver.generateQuery(clasz, query, false, list, list, list, list);
	}
}
