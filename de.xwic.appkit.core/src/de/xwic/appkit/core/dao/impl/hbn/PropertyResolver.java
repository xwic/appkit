/*
 * de.xwic.appkit.core.client.view.browser.ColumnInfo
 * Created on 01.06.2005
 *
 */
package de.xwic.appkit.core.dao.impl.hbn;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

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
public class PropertyResolver {

	private String propertyPath;
	private Method readMethods[] = null;
	private Class<?> baseClass = null;
	private DAO dao = null;

	/**
	 * Constructor.
	 * @param property
	 */
	public PropertyResolver(String property) {
		this.propertyPath = property;
	}
	
	/**
	 * Read the property value as text from the element.
	 * @param element
	 * @return
	 */
	public Object getData(Object element) {
		
		try {
			if (baseClass == null || baseClass != element.getClass()) {
				// We have not introspected the beans for the propertyPath yet
				buildReadPath(element);
			}
			
			Object value = element;
			if (readMethods.length != 0) {
				for (int i = 0; i < readMethods.length && value != null; i++) {
					value = readMethods[i].invoke(value, (Object[]) null);
				}
				if (value != null && dao != null) {
					return dao.buildTitle((IEntity)value);
				}
				return value;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			return null;
		}
		
	}
	
	/**
	 * @throws IntrospectionException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void buildReadPath(Object element) throws IntrospectionException {
		StringTokenizer stk = new StringTokenizer(propertyPath, ".");
		readMethods = new Method[stk.countTokens()];
		
		int idx = 0;
		Class<?> clazz = element.getClass();
		try {
			while (stk.hasMoreTokens()) {
				String propertyName = stk.nextToken();
	
				PropertyDescriptor desc = new PropertyDescriptor(propertyName, clazz);
				clazz = desc.getPropertyType();
				readMethods[idx++] = desc.getReadMethod();
			}
			if (IEntity.class.isAssignableFrom(clazz) && !(IPicklistEntry.class.isAssignableFrom(clazz))) {
				dao = DAOSystem.findDAOforEntity((Class<? extends IEntity>) clazz);
			}
		} catch (IntrospectionException ie) {
			// the property is wrong or does not exist on that type.
			readMethods = new Method[0];
		}
		baseClass = element.getClass();
	}

}
