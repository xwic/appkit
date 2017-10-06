package de.xwic.appkit.core.model;

import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IEntityChangeLog;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.util.Picklists;

public class ChangeLogHelper {

	private Object origValue;
	private Object newValue;
	private String fieldNameKey;
	
	/**
	 * @return the origValue
	 */
	public Object getOrigValue() {
		return origValue;
	}
	
	/**
	 * @param origValue the origValue to set
	 */
	public void setOrigValue(Object origValue) {
		this.origValue = origValue;
	}
	
	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}
	
	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	
	/**
	 * @return the fieldNameKey
	 */
	public String getFieldNameKey() {
		return fieldNameKey;
	}

	
	/**
	 * @param fieldNameKey the fieldNameKey to set
	 */
	public void setFieldNameKey(String fieldNameKey) {
		this.fieldNameKey = fieldNameKey;
	}
	
	
	/**
	 * @param originalValue
	 * @param sb1
	 */
	public static String generateSetChangeString(Object originalSetValue) {
		StringBuilder sb1 = new StringBuilder();
		boolean isFirst = true;
		DAO<?> dao = null;
		if (originalSetValue != null) {
			Set<?> set = (Set<?>) originalSetValue;
			for (Object origObj : set) {
				if (!isFirst) {
					sb1.append(", ");
				}
				isFirst = false;

				if (origObj instanceof IPicklistEntry) {
					IPicklistEntry pe = (IPicklistEntry) origObj;
					sb1.append(Picklists.getTextEn(pe));
				} else if (origObj instanceof IEntity) {
					if (dao == null) {
						dao = DAOSystem.findDAOforEntity(((IEntity) origObj).type());
					}
					sb1.append(dao.buildTitle((IEntity) origObj));
				} else {
					sb1.append(origObj.toString());
				}
			}
		}
		return sb1.toString();
	}
	
	/**
	 * @param changeLogList
	 * @param changeLog
	 * @param bundle
	 */
	public static void fillChangeLogEntries(List<ChangeLogHelper> changeLogList, IEntityChangeLog changeLog, Bundle bundle) {
		if (!changeLogList.isEmpty()) {
			for (ChangeLogHelper helper : changeLogList) {
				addChangeLogEntry(helper.getOrigValue(), helper.getNewValue(), helper.getFieldNameKey(), bundle, changeLog);
			}
		}
	}
	
	/**
	 * Adds a new change log entry.
	 * 
	 * @param originalValue
	 * @param newValue
	 * @param fieldNameKey
	 */
	public static void addChangeLogEntry(Object originalValue, Object newValue, String fieldNameKey, Bundle bundle, IEntityChangeLog changeLog) {
		String fieldName = bundle.getString(fieldNameKey);
		Object sample = originalValue == null ? newValue : originalValue;
		if (sample instanceof IPicklistEntry) {
			changeLog.evaluate(fieldName, originalValue, newValue);
		} else if (sample instanceof IEntity) {
			DAO<?> dao = DAOSystem.findDAOforEntity(((IEntity) sample).type());
			changeLog.evaluate(fieldName, originalValue != null ? dao.buildTitle((IEntity) originalValue) : "",
					newValue != null ? dao.buildTitle((IEntity) newValue) : "");
		} else if (sample instanceof Set) {
			String sb1 = ChangeLogHelper.generateSetChangeString(originalValue);
			String sb2 = ChangeLogHelper.generateSetChangeString(newValue);

			changeLog.evaluate(fieldName, sb1, sb2);
		} else {
			changeLog.evaluate(fieldName, originalValue, newValue);
		}

	}
}
