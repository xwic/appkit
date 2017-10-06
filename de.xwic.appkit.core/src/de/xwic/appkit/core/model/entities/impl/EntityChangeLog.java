/**
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONWriter;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.daos.IEntityChangeLogDAO;
import de.xwic.appkit.core.model.entities.IEntityChangeLog;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.util.Picklists;
import de.xwic.appkit.core.util.Equals;


/**
 * Stores changes made to entities as a textual summary. The change information
 * is stored as a text field only that gives a summary of the change.
 * @author lippisch
 */
public class EntityChangeLog extends Entity implements IEntityChangeLog {

	private static final Log log = LogFactory.getLog(EntityChangeLog.class);

	private String entityType;
	private long entityId;
	private IMitarbeiter person;

	private String changeNotes;

	// ---- INTERNAL fields ---- //
	private boolean closed = false;
	private boolean changed = false;
	private JSONWriter json = null;
	private PrintWriter pw = null;
	private ByteArrayOutputStream bos = null;

	private String agent;

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.IEntityChangeLog#evaluate(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(String fieldName, Object oldValue, Object newValue) {

		if (!Equals.equal(oldValue, newValue)) {
			appendChange(fieldName, oldValue, newValue);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.IEntityChangeLog#appendChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void appendChange(String fieldName, Object oldValue, Object newValue) {

		if (closed) {
			throw new IllegalStateException("The changes have already been saved. Append no longer possible.");
		}
		try {
			if (json == null) {
				bos = new ByteArrayOutputStream();
				pw = new PrintWriter(bos);
				json = new JSONWriter(pw);
				json.object();
				json.key("data");
				json.array();
			}
			changed = true;

			String type = null;
			String oldValData;
			String newValData;

			Object sample = oldValue == null ? newValue : oldValue;
			// determine type
			if (sample instanceof Double) {
				type = "Double";
				oldValData = oldValue != null ? Double.toString((Double) oldValue) : "";
				newValData = newValue != null ? Double.toString((Double) newValue) : "";
			} else if (sample instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				oldValData = oldValue != null ? sdf.format((Date) oldValue) : "";
				newValData = newValue != null ? sdf.format((Date) newValue) : "";
			} else if (sample instanceof IPicklistEntry) {
				type = "IPicklistEntry";
				oldValData = oldValue != null ? Picklists.getTextEn((IPicklistEntry) oldValue) : "";
				newValData = newValue != null ? Picklists.getTextEn((IPicklistEntry) newValue) : "";
			} else {
				// Any basic type is stored as string, which includes Integer, Long etc.
				oldValData = oldValue != null ? String.valueOf(oldValue) : "";
				newValData = newValue != null ? String.valueOf(newValue) : "";
				oldValData = StringEscapeUtils.escapeHtml(oldValData);
				if (newValData.length() > 255) {
					newValData = newValData.substring(0, 255) + "...";
				}
				newValData = StringEscapeUtils.escapeHtml(newValData);
			}


			json.object();
			if (type != null) {
				json.key("type");
				json.value(type);
			}
			json.key("field");
			json.value(fieldName);

			if (oldValData.length() < 255) {
				// do not append if length was > 255
				json.key("old");
				json.value(oldValData);
			}
			json.key("new");
			json.value(newValData);
			json.endObject();

		} catch (JSONException je) {
			// translate a JSONException into a RuntimeException
			throw new RuntimeException("Error generating change log", je);
		}

	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.IEntityChangeLog#saveIfChanged()
	 */
	@Override
	public boolean saveIfChanged() {
		if (changed) {

			if (getEntityId() == 0) {
				throw new IllegalArgumentException("Missing reference entity ID. The reference entity was probably not saved yet, can't store log.");
			}

			try {
				json.endArray();
				json.endObject();
			} catch (JSONException je) {
				// translate a JSONException into a RuntimeException
				throw new RuntimeException("Error generating change log", je);
			}
			pw.flush();

			String data = bos.toString();
			if (data.length() > 4000) {
				log.warn("Data truncation! Need to truncate change log as it got too big: " + data);
				// Replace with a data overflow message
				data = "{\"data\":[{\"system\":\"Data Overflow\"}]}";
			}
			setChangeNotes(data);
			closed = true;
			DAOSystem.getDAO(IEntityChangeLogDAO.class).update(this);;
			return true;
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#getEntityType()
	 */
	@Override
	public String getEntityType() {
		return entityType;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#setEntityType(java.lang.String)
	 */
	@Override
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#getEntityId()
	 */
	@Override
	public long getEntityId() {
		return entityId;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#setEntityId(int)
	 */
	@Override
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#getPerson()
	 */
	@Override
	public IMitarbeiter getPerson() {
		return person;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#setPerson(de.xwic.appkit.core.model.entities.IMitarbeiter)
	 */
	@Override
	public void setPerson(IMitarbeiter person) {
		this.person = person;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#getChangeNotes()
	 */
	@Override
	public String getChangeNotes() {
		return changeNotes;
	}


	/* (non-Javadoc)
	 * @see com.netapp.pulse.model.entities.impl.IEntityChangeLog#setChangeNotes(java.lang.String)
	 */
	@Override
	public void setChangeNotes(String changeNotes) {
		this.changeNotes = changeNotes;
	}

	/**
	 * @return the agent
	 */
	@Override
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	@Override
	public void setAgent(String agent) {
		this.agent = agent;
	}

}
