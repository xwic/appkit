/**
 *
 */
package de.xwic.appkit.core.model.entities;

import org.json.JSONException;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author lippisch
 *
 */
public interface IEntityChangeLog extends IEntity {

	public final String PROJECT_STATUS = "Project Status";
	public final String TASK_STATUS = "Task Status";
	public final static String AGENT_PSA = "PSA SYSTEM";
	public final static String AGENT_PULSE = "Pulse Agent";
	public final static String AGENT_CAP = "CAP Agent";

	/**
	 * Evaluates two values if they are equal and adds an entry in the log if they have been changed.
	 *
	 * @param fieldName
	 * @param oldValue
	 * @param newValue
	 * @return True if the fields have been identified as changed.
	 * @throws JSONException
	 */
	public abstract boolean evaluate(String fieldName, Object oldValue, Object newValue);

	/**
	 * Append a changed field without comparing the values. This does still trigger the 'changed' flag, so
	 * that saveIfChanged() will actually save the data.
	 * @param fieldName
	 * @param oldValue
	 * @param newValue
	 * @throws JSONException
	 */
	public abstract void appendChange(String fieldName, Object oldValue, Object newValue);

	/**
	 * Save the record if a change was detected.
	 * @return true if the record was saved as changes have been detected.
	 * @throws JSONException
	 */
	public abstract boolean saveIfChanged();

	/**
	 * @return the entityType
	 */
	public abstract String getEntityType();

	/**
	 * @param entityType the entityType to set
	 */
	public abstract void setEntityType(String entityType);

	/**
	 * @return the entityId
	 */
	public abstract long getEntityId();

	/**
	 * @param entityId the entityId to set
	 */
	public abstract void setEntityId(long entityId);

	/**
	 * @return the person
	 */
	public abstract IMitarbeiter getPerson();

	/**
	 * @param person the person to set
	 */
	public abstract void setPerson(IMitarbeiter person);

	/**
	 * @return the changeNotes
	 */
	public abstract String getChangeNotes();

	/**
	 * @param changeNotes the changeNotes to set
	 */
	public abstract void setChangeNotes(String changeNotes);

	/**
	 * @return the agent
	 */
	public String getAgent();

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent);
}