/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.ColumnInfo
 * Created on Mar 20, 2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.webbase.viewer.columns;

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
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Holds information about a column in a table and transforms a property 
 * to text. This class is able to read the String value of a property from
 * a row object. The required information to read the properties is cached
 * within this object. It is beeing build up the first time getText(..) is called. The
 * object type for any further call must be the same as the first one.
 * 
 * @author Florian Lippisch
 */
public class TableColumnInfo {

	private final static Object[] noArgs = new Object[0];

	private ListColumn column = null;
	private Method readMethods[] = null;
	private Class<?> baseClass = null;
	private PropertyEditor propertyEditor = null;
	private int dataIdx = 0;

	private Locale locale = null;
	
	public TableColumnInfo(Locale userLocale) {
		if (userLocale == null) {
			locale = Locale.getDefault();
		} else {
			locale = userLocale;
		}
	}
	
	/**
	 * Read the property value as text from the element.
	 * @param element
	 * @return
	 */
	public Object getData(Object element) {
		
		try {
			Object value;
		
			if (element instanceof Object[]) {
				Object[] data = (Object[])element;
				if (dataIdx < data.length) {
					value = data[dataIdx];
				} else {
					value = null;
				}
			} else {
				if (baseClass == null || baseClass != element.getClass()) {
					// We have not introspected the beans for the propertyPath yet
					buildReadPath(element);
				}
				
				value = element;
				for (int i = 0; i < readMethods.length && value != null; i++) {
					value = readMethods[i].invoke(value, noArgs);
				}
			}
			return value;
		} catch (InvocationTargetException ite) {
			if (ite.getTargetException() instanceof SecurityException) {
				return "[N/A]";
			}
			return "ERR: " + ite.getTargetException();
		} catch (Exception e) {
			return "ERR: " + e;
		}
		
	}
	/**
	 * Returns the value as String.
	 * @param element
	 * @return
	 */
	public String getText(Object element) {
		return toString(getData(element));
	}	
	
	private String toString(Object value) {
		String text;
		
		DateFormat dateFormat;
		if(Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
			dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		}else {
			dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		}
		
		if (value == null) {
			text = "";
		} else if (value instanceof Date) {
			// special treatment for date objects
			text = dateFormat.format((Date)value);
		} else if (value instanceof Calendar) {
			Calendar c = (Calendar)value;
			text = dateFormat.format(c.getTime());
		} else if (value instanceof Long) {
			NumberFormat nf = NumberFormat.getNumberInstance(locale);
			text = nf.format(((Long)value).longValue());
		} else if (value instanceof Double) {
			NumberFormat nf = NumberFormat.getNumberInstance(locale);
			nf.setMinimumFractionDigits(2);
			text = nf.format(((Double)value).doubleValue());
		} else if (propertyEditor != null) {
			propertyEditor.setValue(value);
			text = propertyEditor.getAsText();
		} else if (value instanceof IPicklistEntry) {
			text = ((IPicklistEntry)value).getBezeichnung(locale.getLanguage());
		} else if (value instanceof IEntity) {
			IEntity entity = (IEntity)value;
			DAO dao = DAOSystem.findDAOforEntity(entity.type());
			text = dao.buildTitle(entity);
		} else if (value instanceof Set<?>) {
			// set of picklist entries
			StringBuffer sb = new StringBuffer();
			for (Iterator<?> it = ((Set<?>)value).iterator(); it.hasNext(); ) {
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
	
}

