/**
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import static de.xwic.appkit.core.util.CollectionUtil.cloneToSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;

/**
 * @author Alexandru Bledea
 * @since May 28, 2014
 */
public class PropertyQueryTest {

	private static final Class<IMitarbeiter> MITARBEITER = IMitarbeiter.class;

	private static final IEntityQueryResolver resolver = new PropertyQueryResolver();
	private static final String BASIC_QUERY = " FROM de.xwic.appkit.core.model.entities.IMitarbeiter AS obj \n WHERE  obj.deleted = 0 ";

	/**
	 * 
	 */
	@Test
	public void testSimpleQuery() {
		assertEquals(BASIC_QUERY + " ", onlyQuery(new PropertyQuery()));
	}

	/**
	 * 
	 */
	@Test
	public void testInQuery() {
		{
			final List<QueryElement> values = new ArrayList<QueryElement>();
			final PropertyQuery pq = new PropertyQuery();
			final List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);
			pq.addIn("id", ids);

			final String expectedQuery = BASIC_QUERY + "AND (obj.id IN (?,?,?,?,?)) ";
			assertEquals(expectedQuery, generate(pq, values));

//			test arguments
			assertSame(1, values.size());
			final Set<Integer> ints = new HashSet<Integer>();
			for (final QueryElement integer : values) {
				ints.addAll((Collection<Integer>) integer.getValue());
			}
			assertEquals(cloneToSet(ids), ints);
		}

		{
			final List<QueryElement> values = new ArrayList<QueryElement>();
			final PropertyQuery pq = new PropertyQuery();
			final List<Integer> ids = new ArrayList<Integer>();
			for (int i = 0; i < 1001; i++) {
				ids.add(i);
			}
			pq.addIn("id", ids);

			final String thousandQs = StringUtils.join(Collections.nCopies(1000, '?'), ',');
			final String expectedQuery = BASIC_QUERY + "AND (obj.id IN (" + thousandQs + ") OR obj.id IN (?)) ";
			assertEquals(expectedQuery, generate(pq, values));

//			test arguments
			assertSame(2, values.size());

//			test arguments
			final Set<Integer> ints = new HashSet<Integer>();
			for (final QueryElement integer : values) {
				ints.addAll((Collection<Integer>) integer.getValue());
			}
			assertEquals(cloneToSet(ids), ints);
		}
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNullQuery() {
		onlyQuery(null);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testWrongQuery() {
		onlyQuery(new EntityQuery() {
		});
	}

	/**
	 * 
	 */
	@Test
	public void testCollectionsEmptyNotEmpty() {
		final PropertyQuery pq = new PropertyQuery();
		pq.addEmpty("shouldbeempty");
		pq.addNotEmpty("shouldnotbeempty");
		pq.addOrEmpty("orshouldbeempty");
		pq.addNotEmpty("orshouldnotbeempty");
		final String expected = BASIC_QUERY
				+ "AND obj.shouldbeempty IS EMPTY AND obj.shouldnotbeempty IS NOT EMPTY OR obj.orshouldbeempty IS EMPTY AND obj.orshouldnotbeempty IS NOT EMPTY ";
		assertEquals(expected, onlyQuery(pq));
	}

	/**
	 * @param MITARBEITER
	 * @param query
	 * @return
	 */
	private static String onlyQuery(final EntityQuery query) {
		return generate(query, new ArrayList<QueryElement>());
	}

	/**
	 * @param query
	 * @param values
	 * @param customWhereClauses
	 * @param customFromClauses
	 * @param customValues
	 */
	private static String generate(final EntityQuery query, final List<QueryElement> values) {
		final List<String> customWhereClauses = new ArrayList<String>();
		final List<String> customFromClauses = new ArrayList<String>();
		final List<Object> customValues = new ArrayList<Object>();
		return resolver.generateQuery(MITARBEITER, query, false, values, customFromClauses, customWhereClauses, customValues);
	}

}
