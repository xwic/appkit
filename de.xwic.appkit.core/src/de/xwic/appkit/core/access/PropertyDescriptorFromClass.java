/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 *
 */
package de.xwic.appkit.core.access;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.util.Function;
import de.xwic.appkit.core.util.MapUtil;

/**
 * This class is used to generate a map of Class<IEntity>, Map<String, PropertyDescriptor>
 *
 * @author Alexandru Bledea
 * @since Jan 10, 2014
 */
final class PropertyDescriptorFromClass implements Function<Class<IEntity>, Map<String, PropertyDescriptor>> {

	/**
	 *
	 */
	private PropertyDescriptorFromClass() {
	}

	/**
	 * used to map property descriptor name to property descriptors
	 */
	private static final Function<PropertyDescriptor, String> NAME_EXTRACTOR = new Function<PropertyDescriptor, String>() {
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
		//return MapUtil.wrapAI(new HashMap<Class<IEntity>, Map<String, PropertyDescriptor>>(), new PropertyDescriptorFromClass());
		return MapUtil.aiMap(new PropertyDescriptorFromClass());
	}

}
