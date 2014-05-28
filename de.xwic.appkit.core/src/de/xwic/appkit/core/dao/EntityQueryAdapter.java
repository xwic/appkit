/**
 *
 */
package de.xwic.appkit.core.dao;

import java.util.List;

import de.xwic.appkit.core.model.queries.QueryElement;

/**
 * @author Alexandru Bledea
 * @since May 28, 2014
 */
public abstract class EntityQueryAdapter implements IEntityQueryResolver {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#generateQuery(java.lang.Class, de.xwic.appkit.core.dao.EntityQuery, boolean, java.util.List, java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public String generateQuery(final Class<? extends Object> entityClass, final EntityQuery query, final boolean justCount,
			final List<QueryElement> values, final List<String> customFromClauses, final List<String> customWhereClauses,
			final List<Object> customValues) {
		throw new UnsupportedOperationException();
	}

}
