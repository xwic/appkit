/**
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IEntityChangeLogDAO;
import de.xwic.appkit.core.model.entities.IEntityChangeLog;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.impl.EntityChangeLog;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 *
 * @author Florian Lippisch
 */
public class EntityChangeLogDAO extends AbstractDAO<IEntityChangeLog, EntityChangeLog> implements IEntityChangeLogDAO {

	/**
	 *
	 */
	public EntityChangeLogDAO() {
		super(IEntityChangeLog.class, EntityChangeLog.class);
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.dao.IEntityChangeLogDAO#startLog(de.xwic.appkit.core.dao.IEntity, de.xwic.appkit.core.model.entities.IMitarbeiter, java.lang.String)
	 */
	@Override
	public IEntityChangeLog startLog(IEntity reference, IMitarbeiter person, String agent) {
		if (reference == null) {
			throw new NullPointerException("Entity Reference can not be null.");
		}

		IEntityChangeLog ecl = createEntity();
		ecl.setEntityType(reference.type().getName());
		ecl.setEntityId(reference.getId());
		ecl.setPerson(person);
		ecl.setAgent(agent);

		return ecl;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.dao.IEntityChangeLogDAO#getLog(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public List<IEntityChangeLog> getLog(IEntity reference) {
		return getLogsOrdered(reference, PropertyQuery.SORT_DIRECTION_DOWN);		
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.dao.IEntityChangeLogDAO#getLog(de.xwic.appkit.core.dao.IEntity, int)
	 */
	@Override
	public List<IEntityChangeLog> getLogsOrdered(IEntity reference, int sortOrder) {
		if (reference == null) {
			throw new NullPointerException("Entity Reference can not be null.");
		}

		PropertyQuery query = new PropertyQuery();
		query.addEquals("entityType", reference.type().getName());
		query.addEquals("entityId", reference.getId());
		query.setSortField("createdAt");
		query.setSortDirection(sortOrder);

		return getEntities(null, query);
	}

}
