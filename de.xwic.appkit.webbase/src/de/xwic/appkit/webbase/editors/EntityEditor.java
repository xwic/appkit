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
package de.xwic.appkit.webbase.editors;

import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.editor.ESubTab;
import de.xwic.appkit.core.config.editor.ETab;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.model.EntityModelException;
import de.xwic.appkit.webbase.controls.comment.EntityShoutBox;
import de.xwic.appkit.webbase.editors.builders.Builder;
import de.xwic.appkit.webbase.editors.builders.BuilderRegistry;
import de.xwic.appkit.webbase.editors.builders.EContainerBuilder;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.mappers.MappingException;

/**
 * Generates a form to edit an entity based upon a user configuration.
 * 
 * @author lippisch
 */
public class EntityEditor extends ControlContainer {

	private GenericEditorInput input = null;
	private EditorContext context = null;
	private TabStrip bottomTabs;

	/**
	 * @param container
	 * @param name
	 * @param input
	 * @throws EntityModelException
	 * @throws ConfigurationException
	 */
	public EntityEditor(IControlContainer container, String name, GenericEditorInput input)
			throws ConfigurationException, EntityModelException {
		super(container, name);
		this.input = input;
		this.context = new EditorContext(input, getSessionContext().getLocale().getLanguage());

		createControls();

		context.addEditorListener(new EditorAdapter() {

			@Override
			public void entityLoaded(EditorEvent event) {
				if (bottomTabs != null) {
					bottomTabs.setVisible(!context.isNew() && !bottomTabs.getTabs().isEmpty());
				}
			}
		});

	}

	/**
	 * Loads the data from the entity that is to be edited. This needs to be invoked by the editor host as the last step of the
	 * initialization.
	 */
	public void loadFromEntity() {

		try {
			context.loadFromEntity();
		} catch (MappingException e) {
			getSessionContext().notifyMessage("Error loading data from entity: " + e);
			log.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.wap.core.Part#createControls(de.jwic.base.IControlContainer)
	 */
	public void createControls() {
		try {
			EditorConfiguration config = input.getConfig();
			List<ETab> tabs = config.getTabs();
			//setTitle(input.getName());

			TabStrip mainTabs = new TabStrip(this, "main");

			for (ETab eTab : tabs) {
				Tab tab = mainTabs.addTab(eTab.getTitle());
				EditorContentPage page = new EditorContentPage(tab, null);
				page.setTitle(eTab.getTitle());

				context.setCurrPage(page);
				createPage(page, eTab);
				context.setCurrPage(null);

				context.registerWidget(tab, eTab);
			}

			bottomTabs = new TabStrip(this, "bottom");
			List<ESubTab> subTabs = config.getSubTabs();
			for (ESubTab eSubTab : subTabs) {
				Tab tab = bottomTabs.addTab(eSubTab.getTitle());
				EditorContentPage page = new EditorContentPage(tab, null);
				page.setTitle(eSubTab.getTitle());

				context.setCurrPage(page);
				createPage(page, eSubTab);
				context.setCurrPage(null);

				context.registerWidget(tab, eSubTab);
			}

			bottomTabs.setVisible(false);
			
			if (showComment()) {
				new EntityShoutBox(this, "entityShoutBox", context.getModel().getOriginalEntity());
			}

		} catch (Exception e) {
			log.error("Error opening editor", e);
			getSessionContext().notifyMessage("An error occured while initializing controls from the editor:" + e);
		}
	}
	
	public boolean showComment() {
		EditorConfiguration editorConfig = context.getEditorConfiguration();
		return editorConfig.getProperties().containsKey(EditorConfiguration.SHOW_COMMENT)
				&& editorConfig.getProperties().get(EditorConfiguration.SHOW_COMMENT).toLowerCase().equals("true");
	}

	/**
	 * creates a page.
	 * 
	 * @param page
	 * @param eTab
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createPage(EditorContentPage page, UIElement eTab) {
		Builder builder = BuilderRegistry.getBuilderByClass(EContainerBuilder.class);
		builder.buildComponents(eTab, page, context);
	}

	/**
	 * @return the context
	 */
	public EditorContext getContext() {
		return context;
	}

}
