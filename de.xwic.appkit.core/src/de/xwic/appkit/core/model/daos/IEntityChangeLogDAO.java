/**
 *
 */
package de.xwic.appkit.core.model.daos;

import java.util.List;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IEntityChangeLog;
import de.xwic.appkit.core.model.entities.IMitarbeiter;

/**
 * Stores and changes to other entities in textual fashion. Use the DAO to create the log as below:
 * <code>
 *    IEntityChangeLogDAO eclDAO = PulseModelConfig.getEntityChangeLogDAO();
 *    IEntityChangeLog entry = eclDAO.startLog(myEntity, currentPerson);
 *    ..
 *    entry.evaluate("Customer Name", oldName, newName);
 *    entry.evaluate("Status", oldStatus, newStatus);
 *    entry.evaluate("Comment", oldComment, newComment);
 *    ..
 *    entry.saveIfChanged();
 * </code>
 *
 * @author Florian Lippisch
 */
public interface IEntityChangeLogDAO extends DAO<IEntityChangeLog> {

	/**
	 * Create a new IEntityChangeLog entity with a reference to the given entity.
	 * @param reference
	 * @param person
	 * @param agent
	 * @return
	 */
	public IEntityChangeLog startLog(IEntity reference, IMitarbeiter person, String agent);

	/**
	 * Returns the change log for the given entity. The log entries are ordered DESC by default 
	 * 
	 * @param reference
	 * @return
	 */
	public List<IEntityChangeLog> getLog(IEntity reference);
	
	/**
	 * Returns the change log for the given entity. The log entries are ordered as specified by the user
	 * 
	 * @param reference
	 * @param sortOrder
	 * @return
	 */
	public List<IEntityChangeLog> getLogsOrdered(IEntity reference, int sortOrder);

}
