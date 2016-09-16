/*
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
**/
package de.xwic.appkit.webbase.editors.mappers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.editors.EditorConfigurationException;
import de.xwic.appkit.webbase.editors.builders.Builder;
import de.xwic.appkit.webbase.editors.builders.BuilderRegistry;

import javax.swing.text.html.HTML;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates a mapper instance of the specified type. This factory knows only the
 * standard mappers. Custom mappers need to be created during the build phase.
 * 
 * @author Lippisch
 */
public class MapperFactory {

	public final static String EXP_ENTITY_EDITOR_MAPPER = "entityEditorMapper";

	private final static Log log = LogFactory.getLog(MapperFactory.class);

	private static MapperFactory instance = null;
	
	private Map<String, Class<? extends PropertyMapper<?>>> knownMappers = new HashMap<String, Class<? extends PropertyMapper<?>>>();
	
	/**
	 * Private constructor, turning this class into a singleton.
	 */
	@SuppressWarnings("unchecked")
	private MapperFactory() {
		
		knownMappers.put(InputboxMapper.MAPPER_ID, InputboxMapper.class);
		knownMappers.put(HtmlEditorMapper.MAPPER_ID, HtmlEditorMapper.class);
		knownMappers.put(NumericInputboxMapper.MAPPER_ID, NumericInputboxMapper.class);
		knownMappers.put(PicklistEntryMapper.MAPPER_ID, PicklistEntryMapper.class);
		knownMappers.put(PicklistEntrySetMapper.MAPPER_ID, PicklistEntrySetMapper.class);
		knownMappers.put(YesNoRadioGroupMapper.MAPPER_ID, YesNoRadioGroupMapper.class);
		knownMappers.put(EntitySelectorMapper.MAPPER_ID, EntitySelectorMapper.class);
		knownMappers.put(DatePickerMapper.MAPPER_ID, DatePickerMapper.class);
		knownMappers.put(ListViewMapper.MAPPER_ID, ListViewMapper.class);
		
		
		// register 'extensions'
		List<IExtension> extensions = ExtensionRegistry.getInstance().getExtensions(EXP_ENTITY_EDITOR_MAPPER);
		for (IExtension ex : extensions) {
			try {
				String mapperClazz = ex.getClassName();
				Class<PropertyMapper<?>> clazz = (Class<PropertyMapper<?>>) Class.forName(mapperClazz);
				if (knownMappers.containsKey(ex.getId())) {
					log.info("Mapper with ID '" + ex.getId() + "' is already registered. Mapper will be overriden.");
				}
				knownMappers.put(ex.getId(), clazz);
				log.info("Registered mapper with id " + ex.getId());
			} catch (Exception e) {
				log.error("Error registering custom builder id '" + ex.getId() + "'", e);
			}
		}

	}

	/**
	 * Returns the single instance of the StandardMapperFactory.
	 * @return
	 */
	public static MapperFactory instance() {
		if (instance == null) {
			synchronized(MapperFactory.class) {
				if (instance == null) {
					instance = new MapperFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a mapper instance fo the given type.
	 * @param mapperId
	 * @return
	 * @throws EditorConfigurationException 
	 */
	@SuppressWarnings("rawtypes")
	public PropertyMapper<IControl> createMapper(String mapperId, EntityDescriptor descriptor) throws EditorConfigurationException {
		
		Class<? extends PropertyMapper<?>> clazz = knownMappers.get(mapperId);
		if (clazz == null) {
			throw new IllegalArgumentException("A mapper with the id '" + mapperId + "' is unknown.");
		}
		
		try {
			Constructor<? extends PropertyMapper> constructor = clazz.getConstructor(EntityDescriptor.class);
			@SuppressWarnings("unchecked")
			PropertyMapper<IControl> mapper = constructor.newInstance(descriptor);
			return mapper;

		} catch (Exception e) {
			throw new EditorConfigurationException("Cannot create mapper id '" + mapperId + "' (" + clazz.getName() + ")", e);
		}
		
	}
	
}
