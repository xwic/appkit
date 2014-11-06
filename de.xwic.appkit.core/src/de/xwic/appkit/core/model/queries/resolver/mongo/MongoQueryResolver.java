/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.HsqlQueryResolver
 * Created on 12.09.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.mongo;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.impl.mongo.MongoUtil;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.core.model.queries.resolver.hbn.QueryResolver;
import de.xwic.appkit.core.model.util.EntityUtil;
import de.xwic.appkit.core.util.CollectionUtil;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * @author Vitaliy Zhovtyuk
 */
public class MongoQueryResolver extends QueryResolver<Query> {
    @Override
    public Query resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
        Query q = MongoUtil.createQuery(EntityUtil.type(entityClass));
        PropertyQuery propertyQuery = (PropertyQuery) entityQuery;
        resolveFilter(q, propertyQuery);
        List<String> columnsList = propertyQuery.getColumns();
        if (!CollectionUtil.isEmpty(columnsList)) {
            final String[] columns = new String[columnsList.size()];
            columnsList.toArray(columns);
            q.retrievedFields(false, columns);
        }

        return q;
    }

    /**
     * Resolve nested queries to mongo query filter.
     *
     * @param q             mongo query
     * @param propertyQuery property query
     */
    private void resolveFilter(Query q, PropertyQuery propertyQuery) {
        List<QueryElement> queryElementList = propertyQuery.getElements();
        for (QueryElement queryElement : queryElementList) {
            if (queryElement.getSubQuery() != null) {
                resolveFilter(q, queryElement.getSubQuery());
            }
            if (queryElement.getPropertyName() != null) {
                q.filter("entity." + queryElement.getPropertyName(), queryElement.getValue());
            }
        }
    }
}
