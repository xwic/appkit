/**
 * 
 */
package de.xwic.appkit.webbase.controls.changelog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.Field;
import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IEntityChangeLogDAO;
import de.xwic.appkit.core.model.entities.IEntityChangeLog;


/**
 * Displays changes.
 * @author lippisch
 */
@JavaScriptSupport
public class ChangeLogViewer extends Control {

	private Date createdAt = null;
	private String createdFrom = null;
	private String objectTitle = null;
	
	private Field dataField;

	/**
	 * @param container
	 * @param name
	 * @param entity 
	 */
	public ChangeLogViewer(IControlContainer container, String name, IEntity entity) {
		super(container, name);
		
		dataField = new Field(this, "data");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		
		List<IEntityChangeLog> list = DAOSystem.getDAO(IEntityChangeLogDAO.class).getLog(entity);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (IEntityChangeLog ecl : list) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			
			sb.append("{\"date\":\"");
			sb.append(sdf.format(ecl.getCreatedAt()));
			sb.append("\", \"person\":\"");
			if (ecl.getPerson() != null) {
				sb.append(ecl.getPerson().getNachname() + ", " + ecl.getPerson().getVorname());
			} else {

				String agent = ecl.getAgent();

				if (IEntityChangeLog.AGENT_PSA.equals(agent)) {
					sb.append(IEntityChangeLog.AGENT_PSA);
				} else if (IEntityChangeLog.AGENT_PULSE.equals(agent)) {
					sb.append(IEntityChangeLog.AGENT_PULSE);
				} else {
					sb.append("SYSTEM/PSA");
				}
			}
			sb.append("\", \"data\":");
			sb.append(ecl.getChangeNotes());
			sb.append("}");
		}
		sb.append("]");
		
		dataField.setValue(sb.toString());
		
		createdAt = entity.getCreatedAt();
		createdFrom = entity.getCreatedFrom();
		if ("<no-user>".equals(createdFrom)) {
			createdFrom = "";
		}

		try {
			String type = entity.type().getName();
			EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(type);
			objectTitle = ed.getDomain().getBundle("en").getString(type);
		} catch (ConfigurationException e) {
			objectTitle = "Error resolving name";
			log.error("Error resolving object name", e);
		}
		
	}
	
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * @return the createdFrom
	 */
	public String getCreatedFrom() {
		return createdFrom;
	}
	
	/**
	 * @return the objectTitle
	 */
	public String getObjectTitle() {
		return objectTitle;
	}

	
}
