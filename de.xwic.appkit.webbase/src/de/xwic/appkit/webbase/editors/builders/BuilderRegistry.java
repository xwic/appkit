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
import java.util.Map;

import de.xwic.appkit.core.config.editor.EComposite;
import de.xwic.appkit.core.config.editor.ELabel;
import de.xwic.appkit.core.config.editor.EText;
import de.xwic.appkit.core.config.editor.UIElement;

/**
 * Contains a map of builders.
 * 
 * @author Florian Lippisch
 */
public class BuilderRegistry {

	private Map byElementMap = new HashMap();
	private Map byClassMap = new HashMap();
	private Map byExtension = new HashMap();

	private static BuilderRegistry instance = null;

	/**
	 * Constructor.
	 */
	private BuilderRegistry() {
		// register standard builder
		instance = this;

		registerBuilder(EComposite.class, new EContainerBuilder());
		registerBuilder(EText.class, new EInputboxBuilder());
		registerBuilder(ELabel.class, new ELabelBuilder());
	}

	/**
	 * Register a builder for the specified element class (UIElement subclass).
	 * 
	 * @param elementClass
	 * @param builder
	 */
	public static void registerBuilder(Class elementClass, Builder builder) {
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
	public static void registerBuilder(Builder builder) {
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
	public static void registerBuilder(Builder builder, String extensionId) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		instance.byClassMap.put(builder.getClass(), builder);
		instance.byExtension.put(extensionId, builder);
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
			//TODO: uncomment this when all builders are added
			//throw new IllegalArgumentException("No builder registered for this element type.");
		}
		return builder;
	}

	/**
	 * Returns the builder for the specified element.
	 * 
	 * @param element
	 * @return
	 */
	public static Builder getBuilderByClass(Class builderClass) {
		if (instance == null) {
			instance = new BuilderRegistry();
		}
		Builder builder = (Builder) instance.byClassMap.get(builderClass);
		if (builder == null) {
			//TODO: uncomment this when all builders are added
			//throw new IllegalArgumentException("No such builder registered type.");
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
		Builder builder = (Builder) instance.byExtension.get(extensionId);
		if (builder == null) {
			//TODO: uncomment this when all builders are added
			//throw new IllegalArgumentException("No such builder registered.");
		}
		return builder;
	}

}
