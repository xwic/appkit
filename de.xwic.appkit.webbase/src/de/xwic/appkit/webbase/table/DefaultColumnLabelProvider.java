/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.ColumnInfo
 * Created on Mar 20, 2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.table;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;

import de.jwic.controls.tableviewer.CellLabel;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.appkit.webbase.utils.StringUtils;

/**
 * Holds information about a column in a table and transforms a property 
 * to text. This class is able to read the String value of a property from
 * a row object. The required information to read the properties is cached
 * within this object. It is beeing build up the first time getText(..) is called. The
 * object type for any further call must be the same as the first one.
 * 
 * @author Florian Lippisch
 */
public class DefaultColumnLabelProvider implements IColumnLabelProvider {

	protected final Log log = LogFactory.getLog(getClass());
	
	private final static Object[] noArgs = new Object[0];

	protected ListColumn column = null;
	protected Method readMethods[] = null;
	protected Class<?> baseClass = null;
	protected PropertyEditor propertyEditor = null;
	protected int dataIdx = 0;

	protected Locale locale = null;
	
	protected String myProperty = null;
	protected boolean isPicklistEntry = false;
	protected boolean isCollection = false;
	protected DAO<?> collFetchDAO = null;
	
	protected DateFormat dateFormatter;

	private Class<? extends IEntity> entityClass;
	private boolean customProperty;

	/**
	 * 
	 */
	public DefaultColumnLabelProvider() {
		locale = Locale.getDefault();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IColumnLabelProvider#initialize(java.util.TimeZone, java.util.Locale, de.xwic.appkit.webbase.table.Column)
	 */
	@Override
	public void initialize(TimeZone timeZone, Locale locale, String dateFormat, String timeFormat, Column col) {
		this.locale = locale;
		this.column = col.getListColumn();
		this.entityClass = col.getEntityClass();
		this.customProperty = column.getPropertyId().startsWith("#");

		final Property finalProperty = column.getFinalProperty();
		if (customProperty && null == finalProperty) {
			return;
		}
		if (finalProperty.isEntity() && finalProperty.getEntityType().equals(IPicklistEntry.class.getName())) {
			isPicklistEntry = true;
		}

		switch (finalProperty.getDateType()) {
		case Property.DATETYPE_DATETIME:
			if(dateFormat != null && timeFormat != null){
				dateFormatter = new SimpleDateFormat(dateFormat + " " + timeFormat);
			} else if(Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
				dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			}else {
				dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", locale);
			}
			dateFormatter.setTimeZone(timeZone);
			break;
		case Property.DATETYPE_TIME:
			if(timeFormat != null){
				dateFormatter = new SimpleDateFormat(timeFormat);
			} else if(Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
				dateFormatter = new SimpleDateFormat("HH:mm");
			}else {
				dateFormatter = new SimpleDateFormat("hh:mm a");
			}
			dateFormatter.setTimeZone(timeZone);
			break;
		default:
			if(dateFormat != null){
				dateFormatter = new SimpleDateFormat(dateFormat);
			}else if(Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
				dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
			}else {
				dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", locale);
			}
			break;
			
		}
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IColumnLabelProvider#getCellLabel(de.xwic.appkit.webbase.table.RowData)
	 */
	@Override
	public CellLabel getCellLabel(RowData row) {

		CellLabel cell = new CellLabel();

		Object value = getData(row);
		if (value instanceof Boolean) {
			if (((Boolean)value).booleanValue()) {
				cell.image = ImageLibrary.ICON_CHECKED;
			} else {
				cell.image = ImageLibrary.ICON_UNCHECKED;
			}
			
			cell.object = value; 
		} else {
			cell.text = toString(value);
			cell.object = value;

			if (value instanceof Number) {
				cell.cssClass = "plvalue";
			}
		}
		return cell;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IColumnLabelProvider#getRequiredProperties(java.lang.String)
	 */
	@Override
	public String[] getRequiredProperties(ListColumn listColumn, String propertyId) {
		myProperty = propertyId;
		if (customProperty) {
			return StringUtils.EMPTY_STRING_ARRAY;
		}
		Property prop = listColumn.getFinalProperty();
		if (prop.isEntity()) {
			
			myProperty = propertyId + ".id";
			try {
				EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(prop.getEntityType());
				if (ed.getDefaultDisplayProperty() != null) {
					myProperty = propertyId + "." + ed.getDefaultDisplayProperty();
				}
			} catch (ConfigurationException e) {
				log.warn("Can not find EntityDescriptor for entity type " + prop.getEntityType());
			}
			
		} else if (prop.isCollection() || prop.getEntityType().equals("java.util.Set")) {
			// do not add "this" column
			isCollection = true;
			collFetchDAO = DAOSystem.findDAOforEntity(entityClass);
			return new String[] {};
		}
		return new String[] { myProperty };
	}
	
	/**
	 * Read the property value as text from the element.
	 * @param element
	 * @return
	 */
	public Object getData(RowData element) {
		
		try {
			Object value;
		
			if (element.isArray()) {
				if (isCollection) {
					value = fetchSet(myProperty, element);
				} else {
					value = element.getData(myProperty);
				}
			} else if (element.getEntity() != null) {
				IEntity entity = element.getEntity();
				if (baseClass == null || baseClass != entity.getClass()) {
					// We have not introspected the beans for the propertyPath yet
					buildReadPath(entity);
				}
				
				value = entity;
				for (int i = 0; i < readMethods.length && value != null; i++) {
					value = readMethods[i].invoke(value, noArgs);
				}
			} else {
				value = element.getObject();
			}
			if (isPicklistEntry && value != null && value instanceof Integer) {
				IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
				value = dao.getPickListEntryByID((Integer)value);
			}
			return value;
		} catch (InvocationTargetException ite) {
			log.error("Failed to render column", ite);
			if (ite.getTargetException() instanceof SecurityException) {
				return "[N/A]";
			}
			return "ERR: Failed to render column!";
		} catch (Exception e) {
			log.error("Failed to render column", e);
			return "ERR: Failed to render column!";
		}
		
	}

	/**
	 * @param myProperty2
	 * @param element
	 * @return
	 */
	protected Object fetchSet(String propId, RowData element) {
		
		if (collFetchDAO != null) {
			return collFetchDAO.getCollectionProperty(element.getEntityId(), propId);
		}
		return null;
	}

	/**
	 * Returns the value as String.
	 * @param element
	 * @return
	 */
	public String getText(RowData element) {
		return toString(getData(element));
	}	
	
	private String toString(Object value) {
		String text;
		
		if (value == null) {
			text = "";
		} else if (value instanceof Date) {
			// special treatment for date objects
			text = dateFormatter.format((Date)value);
		} else if (value instanceof Calendar) {
			Calendar c = (Calendar)value;
			text = dateFormatter.format(c.getTime());
		} else if (value instanceof Long) {
			NumberFormat nf = NumberFormat.getNumberInstance(locale);
			text = nf.format(((Long)value).longValue());
		} else if (value instanceof Double) {
			NumberFormat nf = NumberFormat.getNumberInstance(locale);
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			text = nf.format(((Double)value).doubleValue());
		} else if (propertyEditor != null) {
			propertyEditor.setValue(value);
			text = propertyEditor.getAsText();
		} else if (value instanceof IPicklistEntry) {
			text = ((IPicklistEntry)value).getBezeichnung(locale.getLanguage());
		} else if (value instanceof IEntity) {
			IEntity entity = (IEntity)value;
			DAO<?> dao = DAOSystem.findDAOforEntity(entity.type());
			text = dao.buildTitle(entity);
		} else if (value instanceof Collection<?>) {
			// set of picklist entries
			StringBuffer sb = new StringBuffer();
			for (Iterator<?> it = ((Collection<?>)value).iterator(); it.hasNext(); ) {
				Object o = it.next();
				if (sb.length() != 0) {
					sb.append(",");
				}
				sb.append(toString(o));
			}
			text = sb.toString();
		} else if (value instanceof Object[]) {
			StringBuffer sb = new StringBuffer();
			Object[] values = (Object[])value;
			for (int i = 0; i < values.length; i++) {
				if (sb.length() != 0) {
					sb.append(",");
				}
				sb.append(toString(values[i]));
			}
			text = sb.toString();
		} else {
			text = value.toString();
		}
		return text;

	}
	
	/**
	 * @throws IntrospectionException 
	 * 
	 */
	private void buildReadPath(Object element) throws IntrospectionException {
		StringTokenizer stk = new StringTokenizer(column.getPropertyId(), ".");
		readMethods = new Method[stk.countTokens()];
		
		int idx = 0;
		Class<?> clazz = element.getClass();
		while (stk.hasMoreTokens()) {
			String propertyName = stk.nextToken();

			PropertyDescriptor desc = new PropertyDescriptor(propertyName, clazz, makeGetterName(propertyName), null);
			clazz = desc.getPropertyType();
			readMethods[idx++] = desc.getReadMethod();
		}
		propertyEditor = PropertyEditorManager.findEditor(clazz);
		baseClass = element.getClass();
	}

	/**
	 * @param propertyName
	 * @return
	 */
	private String makeGetterName(String s) {
		if (s == null || s.length() == 0) {
		    return s;
		}
		char chars[] = s.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return "is" + new String(chars);
	}
	/**
	 * sets the column
	 * 
	 * @param column
	 */
	public void setColumn(ListColumn column) {
		this.column = column;
	}

	/**
	 * @return Returns the column.
	 */
	public ListColumn getColumn() {
		return column;
	}

	/**
	 * @return the data index
	 */
	public int getDataIdx() {
		return dataIdx;
	}

	/**
	 * sets the data index
	 * 
	 * @param dataIdx
	 */
	public void setDataIdx(int dataIdx) {
		this.dataIdx = dataIdx;
	}

	/**
	 * @param cell
	 * @param label
	 * @return
	 */
	@Override
	public boolean renderExcelCell(Cell cell, CellLabel label) {
		return false;
	}
	
}

