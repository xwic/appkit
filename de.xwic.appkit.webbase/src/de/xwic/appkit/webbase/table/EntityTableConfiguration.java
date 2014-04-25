/**
 * 
 */
package de.xwic.appkit.webbase.table;

import java.util.Locale;
import java.util.TimeZone;

import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Adrian Ionescu
 */
public class EntityTableConfiguration {
	private Class<? extends IEntity> entityClass = null;
	private String listId = Setup.ID_DEFAULT;
	private String viewId = null;
	private PropertyQuery baseFilter = null;
	private PropertyQuery defaultFilter = null;
	private Locale locale;
	private TimeZone timeZone;
	private String dateFormat;
	private String timeFormat;
	
	/**
	 * 
	 */
	public EntityTableConfiguration(Class<? extends IEntity> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * @return the entityClass
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}
	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}
	/**
	 * @param listId the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}
	/**
	 * @return the viewId
	 */
	public String getViewId() {
		return viewId;
	}
	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	/**
	 * @return the basefilter
	 */
	public PropertyQuery getBaseFilter() {
		return baseFilter;
	}
	/**
	 * @param baseFilter the baseFilter to set
	 */
	public void setBaseFilter(PropertyQuery baseFilter) {
		this.baseFilter = baseFilter;
	}
	/**
	 * @return the defaultFilter
	 */
	public PropertyQuery getDefaultFilter() {
		return defaultFilter;
	}
	/**
	 * @param defaultFilter the defaultFilter to set
	 */
	public void setDefaultFilter(PropertyQuery defaultFilter) {
		this.defaultFilter = defaultFilter;
	}
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return
	 */
	public String getTimeFormat() {
		return timeFormat;
	}

	/**
	 * @param timeFormat
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
}
