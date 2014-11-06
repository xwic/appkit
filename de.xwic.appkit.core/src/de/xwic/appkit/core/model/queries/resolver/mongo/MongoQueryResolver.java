/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.HsqlQueryResolver
 * Created on 12.09.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.mongo;

import com.mongodb.MongoClient;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.dao.impl.mongo.EntityWrapper;
import de.xwic.appkit.core.dao.impl.mongo.MongoUtil;
import de.xwic.appkit.core.model.queries.HsqlQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.core.model.queries.resolver.hbn.QueryResolver;
import de.xwic.appkit.core.model.util.DateUtils;
import de.xwic.appkit.core.model.util.EntityUtil;
import de.xwic.appkit.core.util.CollectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FieldCriteria;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.Locale;

/**
 * @author Florian Lippisch
 * @author Christian Jackel
 */
public class MongoQueryResolver extends QueryResolver<Query> {
	/** logger */
	protected final Log log = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Query resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {
        Query q = MongoUtil.createQuery(EntityUtil.type(entityClass));
        PropertyQuery propertyQuery = (PropertyQuery) entityQuery;
        List<QueryElement> queryElementList =  propertyQuery.getElements();
        for(QueryElement queryElement : queryElementList) {
            q.filter("entity." + queryElement.getPropertyName(), queryElement.getValue());
        }
        List<String> columnsList = propertyQuery.getColumns();
        if(!CollectionUtil.isEmpty(columnsList)) {
            final String[] columns = new String[columnsList.size()];
            columnsList.toArray(columns);
            q.retrievedFields(false, columns);
        }

        return q;
	}
}
