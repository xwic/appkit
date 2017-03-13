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
package de.xwic.appkit.webbase.editors.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.editor.EAttachments;
import de.xwic.appkit.core.config.editor.EComposite;
import de.xwic.appkit.core.config.editor.ECustom;
import de.xwic.appkit.core.config.editor.EDate;
import de.xwic.appkit.core.config.editor.EEntityField;
import de.xwic.appkit.core.config.editor.EGroup;
import de.xwic.appkit.core.config.editor.EHtmlEditor;
import de.xwic.appkit.core.config.editor.ELabel;
import de.xwic.appkit.core.config.editor.EListView;
import de.xwic.appkit.core.config.editor.ENumberInputField;
import de.xwic.appkit.core.config.editor.EPicklistCheckbox;
import de.xwic.appkit.core.config.editor.EPicklistCombo;
import de.xwic.appkit.core.config.editor.EPicklistRadio;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.EYesNoRadio;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;

/**
 * Contains a map of builders.
 * 
 * @author Florian Lippisch
 */
public class BuilderRegistry {

	public final static String EXP_ENTITY_EDITOR_BUILDER = "entityEditorBuilder";
	
	private Map<Class<?>, Builder> byElementMap = new HashMap<Class<?>, Builder>();
	private Map<Class<?>, Builder> byClassMap = new HashMap<Class<?>, Builder>();
	private Map<String, Builder> byExtension = new HashMap<String, Builder>();

	private static BuilderRegistry instance = null;
	
	private final static Log log = LogFactory.getLog(BuilderRegistry.class);

	/**
	 * Constructor.
	 */
	private BuilderRegistry() {
		// register standard builder
		instance = this;

		registerBuilder(EComposite.class, new EContainerBuilder<EComposite>());
		registerBuilder(EText.class, new EInputboxBuilder());
		registerBuilder(EEntityField.class, new EEntitySelectorBuilder());
		registerBuilder(EHtmlEditor.class, new EHtmlEditorBuilder());
		registerBuilder(EDate.class, new EDateBuilder());
		registerBuilder(ELabel.class, new ELabelBuilder());
		registerBuilder(EListView.class, new EListViewBuilder());
		registerBuilder(EGroup.class, new EGroupBuilder());
		registerBuilder(EPicklistCombo.class, new EPicklistComboBuilder());
		registerBuilder(EPicklistRadio.class, new EPicklistRadioBuilder());
		registerBuilder(EPicklistCheckbox.class, new EPicklistCheckboxBuilder());
		registerBuilder(EYesNoRadio.class, new EYesNoRadioBuilder());
		registerBuilder(ENumberInputField.class, new ENumberInputBuilder());
		registerBuilder(ECustom.class, new ECustomBuilder());
		registerBuilder(EAttachments.class, new EAttachmentsBuilder());

		
		// register 'extensions'
		List<IExtension> extensions = ExtensionRegistry.getInstance().getExtensions(EXP_ENTITY_EDITOR_BUILDER);
		for (IExtension ex : extensions) {
			try {
				Builder<UIElement> customBuilder = (Builder<UIElement>)ex.createExtensionObject();
				registerBuilder(customBuilder, ex.getId());
			} catch (Exception e) {
				log.error("Error registering custom builder id '" + ex.getId() + "'", e);
			}
		}
		
	}

	/**
	 * Register a builder for the specified element class (UIElement subclass).
	 * 
	 * @param elementClass
	 * @param builder
	 */
	public static void registerBuilder(Class<? extends UIElement> elementClass, Builder<? extends UIElement> builder) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		instance.byElementMap.put(elementClass, builder);
		instance.byClassMap.put(builder.getClass(), builder);
	}

	/**
	 * Register a builder for the specified element class (UIElement subclass).
	 * 
	 * @param elementClass
	 * @param builder
	 */
	public static void registerBuilder(Builder<? extends UIElement> builder) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		instance.byClassMap.put(builder.getClass(), builder);
	}

	/**
	 * Register a builder for the specified element class (UIElement subclass).
	 * 
	 * @param elementClass
	 * @param builder
	 */
	public static void registerBuilder(Builder<? extends UIElement> builder, String extensionId) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		instance.byClassMap.put(builder.getClass(), builder);
		instance.byExtension.put(extensionId, builder);
		log.info("Registered custom builder with id " + extensionId);
	}

	/**
	 * Returns the builder for the specified element.
	 * 
	 * @param element
	 * @return
	 */
	public static Builder getBuilder(UIElement element) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		Builder builder = (Builder) instance.byElementMap.get(element.getClass());
		if (builder == null) {
			throw new IllegalArgumentException("No builder registered for this element type.");
		}
		return builder;
	}

	/**
	 * Returns the builder for the specified element.
	 * 
	 * @param element
	 * @return
	 */
	public static Builder getBuilderByClass(Class<? extends Builder> builderClass) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		Builder builder = instance.byClassMap.get(builderClass);
		if (builder == null) {
			throw new IllegalArgumentException("No such builder registered type.");
		}
		return builder;
	}

	/**
	 * Returns the builder for the specified element.
	 * 
	 * @param element
	 * @return
	 */
	public static Builder getBuilderByExtension(String extensionId) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		Builder builder = instance.byExtension.get(extensionId);
		if (builder == null) {
			throw new IllegalArgumentException("No builder with extension id + '" + extensionId + "' registered.");
		}
		return builder;
	}

}
