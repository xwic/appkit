/**
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import static de.xwic.appkit.core.util.CollectionUtil.cloneToSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.xwic.appkit.core.dao.Entity;
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

	private static final int STACKTRACE_POSITION = getStackTracePosition();

	private static final Class<IMitarbeiter> MITARBEITER = IMitarbeiter.class;

	private static final IEntityQueryResolver resolver = new PropertyQueryResolver();
	private static final String BASIC_QUERY = " FROM de.xwic.appkit.core.model.entities.IMitarbeiter AS obj \n WHERE  obj.deleted = 0 ";
	/**
	 * 
	 */
	@Test
	public void testSimpleQuery() {
		final String generated = generate(new PropertyQuery());
		assertEquals(BASIC_QUERY + " ", generated);
		printQuery(generated);
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
			printQuery(expectedQuery);

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
			printQuery(expectedQuery);

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
	@Test
	public void testNullNotNull() {
		final PropertyQuery pq = new PropertyQuery();
		pq.addEquals("isnull", null);
		pq.addNotEquals("isnotnull", null);
		final String expected = BASIC_QUERY + "AND obj.isnull IS NULL AND obj.isnotnull IS NOT NULL ";
		assertEquals(expected, generate(pq));
		printQuery(expected);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNullQuery() {
		generate(null);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testWrongQuery() {
		generate(new EntityQuery() {
		});
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testBadQueryElement() {
		final PropertyQuery pq = new PropertyQuery();
		pq.addEmpty("something");
		pq.addQueryElement(new QueryElement(Integer.MAX_VALUE, new PropertyQuery()));
		generate(pq);
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
		pq.addOrNotEmpty("orshouldnotbeempty");
		final String expected = BASIC_QUERY
				+ "AND obj.shouldbeempty IS EMPTY AND obj.shouldnotbeempty IS NOT EMPTY OR obj.orshouldbeempty IS EMPTY OR obj.orshouldnotbeempty IS NOT EMPTY ";
		assertEquals(expected, generate(pq));
		printQuery(expected);
	}

	/**
	 * 
	 */
	@Test
	public void testEntityEquals() {
		final PropertyQuery pq = new PropertyQuery();
		pq.addEquals("supervisor.id", 10);

		final Entity supervisor = new Entity();
		supervisor.setId(20);
		pq.addEquals("supervisor", supervisor);

		final List<QueryElement> values = new ArrayList<QueryElement>();
		final String query = generate(pq, values);
		final String expected = BASIC_QUERY + "AND obj.supervisor.id = ? AND obj.supervisor.id = ? ";
		assertEquals(expected, query);
		printQuery(expected);

		for (final QueryElement queryElement : values) {
			final int id = ((Integer) queryElement.getValue()).intValue();
			assertTrue(id == 10 || id == 20);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testColumnsOnly() {
		final PropertyQuery pq = new PropertyQuery();
		pq.setColumns(Collections.singletonList("name"));
		final String actual = generate(pq);
		assertEquals("select obj.id, obj.name" + BASIC_QUERY + " ", actual);
		printQuery(actual);
	}

	/**
	 * 
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testBadNullElement() {
		final PropertyQuery pq = new PropertyQuery();
		pq.addLike("null", null);
		generate(pq);
	}

	@Test
	public void testAutoJoin() {
		final PropertyQuery pq = new PropertyQuery();
		pq.setSortField("ilon.paiva");

		final String[] split = splitBasicQuery();

		final String expected = split[0] + "\n" + " LEFT OUTER JOIN obj.ilon " + "\n" + split[1] + "  order by obj.ilon.paiva asc";
		assertEquals(expected, generate(pq));
		printQuery(expected);
	}

	/**
	 * 
	 */
	@Test
	public void testIgnoreEmptyColumns() {
		final PropertyQuery pq = new PropertyQuery();
		List<String> emptyList = Collections.emptyList();
		pq.setColumns(emptyList);
		final String expected = "select obj.id" + BASIC_QUERY + " ";
		assertEquals(expected, generate(pq));
		printQuery(expected);
	}

	/**
	 * @param query
	 * @return
	 */
	private static String generate(final EntityQuery query) {
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

	/**
	 * 
	 */
	private static void printQuery(final String query) {
		final StringBuilder sb = new StringBuilder("=== ");

//		find the name of the first method in the stack trace that belongs
//		to this class but isn't the current method
		sb.append(getCallerMethodName(Thread.currentThread().getStackTrace()));
		sb.append(" ===\n");
		sb.append(query).append("\n");
		System.out.println(sb.append("\n"));
	}

	/**
	 * @return
	 */
	private static String[] splitBasicQuery() {
		return BASIC_QUERY.split("\n");
	}

	/**
	 * @param stackTrace
	 * @return
	 */
	private static String getCallerMethodName(final StackTraceElement[] stackTrace) {
		if (STACKTRACE_POSITION > stackTrace.length) {
			return "";
		}
		return stackTrace[STACKTRACE_POSITION].getMethodName();
	}

	/**
	 * searches the stacktrace for the first element that
	 * belongs to this class and returns that element + 1
	 * or Integer.MAX_VALUE if this class is not found in the stacktrace
	 * @return
	 */
	private static int getStackTracePosition() {
		final String thisClassName = PropertyQueryTest.class.getName();
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			if (thisClassName.equals(stackTrace[i].getClassName())) {
				return i + 1; // we generally want the element that is a level above the called method
			}
		}
		return Integer.MAX_VALUE;
	}
}
