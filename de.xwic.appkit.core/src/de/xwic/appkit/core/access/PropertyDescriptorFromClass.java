/**
 *
 */
package de.xwic.appkit.core.access;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.util.ILazyEval;
import de.xwic.appkit.core.util.MapUtil;

/**
 * This class is used to generate a map of Class<IEntity>, Map<String, PropertyDescriptor>
 *
 * @author Alexandru Bledea
 * @since Jan 10, 2014
 */
final class PropertyDescriptorFromClass implements ILazyEval<Class<IEntity>, Map<String, PropertyDescriptor>> {

	/**
	 *
	 */
	private PropertyDescriptorFromClass() {
	}

	/**
	 * used to map property descriptor name to property descriptors
	 */
	private static final ILazyEval<PropertyDescriptor, String> NAME_EXTRACTOR = new ILazyEval<PropertyDescriptor, String>() {
		@Override
		public String evaluate(final PropertyDescriptor obj) {
			return obj.getName();
		}
	};

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.ILazyEval#evaluate(java.lang.Object)
	 */
	@Override
	public Map<String, PropertyDescriptor> evaluate(final Class<IEntity> clasz) {
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(clasz);
			final List<PropertyDescriptor> propertyDescriptors = Arrays.asList(beanInfo.getPropertyDescriptors());
			return MapUtil.generateMap(propertyDescriptors, NAME_EXTRACTOR);
		} catch (IntrospectionException ex) {
			throw new IllegalStateException("Failed to read information from " + clasz.getName(), ex);
		}
	}

	/**
	 * @return
	 */
	public static Map<Class<IEntity>, Map<String, PropertyDescriptor>> createMapGenerator() {
		Map<Class<IEntity>, Map<String, PropertyDescriptor>> aiMap = MapUtil.aiMap(new PropertyDescriptorFromClass());
		return aiMap;
	}

}
