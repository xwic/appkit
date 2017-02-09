/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.toolkit.app.modeladapters.BeanAdapter 
 */

package de.xwic.appkit.webbase.toolkit.app.modeladapters;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import de.xwic.appkit.core.util.IModelViewTypeConverter;
import de.xwic.appkit.core.util.typeconverters.BooleanStringConverter;
import de.xwic.appkit.core.util.typeconverters.DoubleStringConverter;
import de.xwic.appkit.core.util.typeconverters.FloatStringConverter;
import de.xwic.appkit.core.util.typeconverters.IntegerStringConverter;
import de.xwic.appkit.core.util.typeconverters.LongStringConverter;

/**
 * Allows the editor toolkit to save and read values from a java bean
 * 
 * @author Andrei Pat
 *
 */
public class EditorToolkitBeanModelAdapter implements IEditorToolkitModelAdapter {

	protected Object modelBean;

	public EditorToolkitBeanModelAdapter(Object modelBean) {
		this.modelBean = modelBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.modeladapters.IEditorToolkitModelAdapter#read(java.lang.String,
	 * de.xwic.appkit.core.util.ITypeConverter)
	 */
	@Override
	public Object read(String propertyName, IModelViewTypeConverter converter) {
		Object value = null;
		try {
			PropertyDescriptor propInfo = new PropertyDescriptor(propertyName, getBeanClazz());
			value = propInfo.getReadMethod().invoke(modelBean, (Object[]) null);
		} catch (Exception ex) {
			throw new RuntimeException("Property not found: " + propertyName, ex);
		}
		if (converter != null) {
			//if we have a type converter registered for the property, convert the entity type to the control value type first
			return converter.convertToViewType(value);
		} else {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.modeladapters.IEditorToolkitModelAdapter#write(java.lang.String, java.lang.Object,
	 * de.xwic.appkit.core.util.ITypeConverter)
	 */
	@Override
	public void write(String propertyName, Object controlValue, IModelViewTypeConverter converter) {
		PropertyDescriptor propInfo = null;

		try {
			propInfo = new PropertyDescriptor(propertyName, getBeanClazz());
		} catch (IntrospectionException ex) {
			throw new RuntimeException("Could find property: " + propertyName + " of entityclass: " + modelBean.getClass().getName(), ex);
		}

		Object value;
		if (converter != null) {
			//if we have a type converter registered for the property, convert the control value type to the entity value type first
			value = converter.convertToModelType(controlValue);
		} else {
			value = autoConvertToEntityType(propInfo, controlValue);
		}

		try {
			propInfo.getWriteMethod().invoke(modelBean, new Object[] { value });
		} catch (Exception ex) {
			throw new RuntimeException("Could not write property: " + propertyName + " of entityclass: " + modelBean.getClass().getName(),
					ex);
		}
	}

	/**
	 * Try some common type mappings
	 * 
	 * @param propInfo
	 * @param controlValue
	 * @return
	 */
	private Object autoConvertToEntityType(PropertyDescriptor propInfo, Object controlValue) {

		Class propertyType = propInfo.getPropertyType();
		Class controlType = null;
		if (controlValue == null) {
			//for primitives, null gets mapped to the default value 
			if (int.class.equals(propertyType) || long.class.equals(propertyType) || double.class.equals(propertyType)
					|| float.class.equals(propertyType) || byte.class.equals(propertyType) || short.class.equals(propertyType)) {
				return 0;
			}
			if (boolean.class.equals(propertyType)) {
				return false;
			}
			if (char.class.equals(propertyType)) {
				return '\u0000';
			}
		} else {
			controlType = controlValue.getClass();
		}

		IModelViewTypeConverter converter = null;
		if (String.class.equals(controlType)) {
			if (Integer.class.equals(propertyType) || int.class.equals(propertyType)) {
				converter = IntegerStringConverter.INSTANCE;
			} else if (Long.class.equals(propertyType) || long.class.equals(propertyType)) {
				converter = LongStringConverter.INSTANCE;
			} else if (Float.class.equals(propertyType) || float.class.equals(propertyType)) {
				converter = FloatStringConverter.INSTANCE;
			} else if (Double.class.equals(propertyType) || double.class.equals(propertyType)) {
				converter = DoubleStringConverter.INSTANCE;
			} else if (Boolean.class.equals(propertyType) || boolean.class.equals(propertyType)) {
				converter = BooleanStringConverter.INSTANCE;
			}
			if (converter != null) {
				return converter.convertToModelType(controlValue);
			}
		}
		return controlValue;
	}

	/**
	 * Get the bean's class. For object proxies, (like entities) we might want to overwrite this default behaviour
	 * 
	 * @return
	 */
	protected Class getBeanClazz() {
		return modelBean.getClass();
	}

}
