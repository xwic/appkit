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
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.actions.Action;
import de.jwic.controls.actions.IAction;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.actions.ICustomEntityActionCreator;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Provides a standard Page 'frame' for the EntityEditor.
 * @author Lippisch
 */
public class EntityEditorPage extends InnerPage {

	public final static String EXTENSION_POINT_EDITOR_EXT = "enityEditorExtension";
	public final static String EXT_ATTR_FOR_ENTITY = "forEntity";
	
	private EntityEditor editor;
	private EditorContext context;

	private IAction actionSave;
	private IAction actionSaveClose;
	private IAction actionClose;
	
	private IAction actionEdit;
	
	private List<IEntityEditorExtension> extensions = new ArrayList<IEntityEditorExtension>();
	private ToolBar toolbar;

	/**
	 * @param container
	 * @param name
	 */
	public EntityEditorPage(IControlContainer container, String name, IEntity entity, EditorConfiguration editorConfig) {
		super(container, name);
		
		
		String title;
		if (entity.getId() == 0) { // new entity
			String entityType = entity.type().getName();
			try {
				EntityDescriptor descriptor = ConfigurationManager.getSetup().getEntityDescriptor(entityType);
				Bundle bundle = descriptor.getDomain().getBundle(getSessionContext().getLocale().getLanguage());
				title = "New " + bundle.getString(entityType) + "*";
			} catch (ConfigurationException ce) {
				log.warn("Error retrieving bundle string for entity", ce);
				title = "Editor";
			}
		} else {
			DAO<? extends IEntity> dao = DAOSystem.findDAOforEntity(entity.type());
			title = dao.buildTitle(entity);
		}
			
		setTitle(title);
		
		createActions();
		createToolbar();
		createContent(entity, editorConfig);
		
		createExtensions(entity.getId());
	}
	
	/**
	 * Search for editor extensions and initialize them.
     * @param id editor entity id
     */
	private void createExtensions(Integer id) {

		// Step 1 - find and instantiate extensions...
		String entityType = context.getEntityDescriptor().getId();
		List<Object> extensionObjs = getExtentions(EXTENSION_POINT_EDITOR_EXT, entityType);
		for (Object extObj : extensionObjs) {
			if (extObj instanceof IEntityEditorExtension) {
				IEntityEditorExtension extension = (IEntityEditorExtension) extObj;
				extensions.add(extension);
			} else {
				log.error("EntityEditorExtension for entity " + entityType + " does not implement IEntityEditorExtension interface.");
			}
		}

		// Step 2 - initialize extensions
		for (IEntityEditorExtension extension : extensions) {
			extension.initialize(context, editor);
			extension.addActions(toolbar);
		}

		List<Object> tabExtensions = getExtentions("entityEditorActions", entityType);
		for (Object ext : tabExtensions) {
			if(ext instanceof ICustomEntityActionCreator) {
				ICustomEntityActionCreator entityActionCreator = (ICustomEntityActionCreator) ext;
				toolbar.addGroup().addAction(entityActionCreator.createAction(ExtendedApplication.getInstance(this).getSite(), entityType, String.valueOf(id)));
			}
		}
	}

	private List<Object> getExtentions(String extPoint, String entityType) {
		List<Object> extension = new ArrayList<Object>();
		List<IExtension> rawExtList = ExtensionRegistry.getInstance().getExtensions(extPoint);
		for (IExtension ext : rawExtList) {

			String applyToEntity = ext.getAttribute(EXT_ATTR_FOR_ENTITY);
			if (applyToEntity != null && entityType.equals(applyToEntity)) {

				Object extObj = null;
				try {
					extObj = ext.createExtensionObject();
				} catch (Exception e) {
					log.error("Error creating EntityEditorExtension for " + entityType + " (id:" + ext.getId() + ")", e);
					// extObj will be null, so we can continue
				}

				if (extObj != null) {
					extension.add(extObj);
				}
			}

		}
        return extension;
	}

	/**
	 * 
	 */
	private void createActions() {

		actionSave = new Action() {
			@Override
			public void run() {
				performSave();
			}
		};
		actionSave.setTitle("Save");
		actionSave.setIconEnabled(ImageLibrary.ICON_SAVE_ACTIVE);
		actionSave.setIconDisabled(ImageLibrary.ICON_SAVE_INACTIVE);

		actionSaveClose = new Action() {
			@Override
			public void run() {
				if (performSave()) {
					closeEditor();
				}
			}
		};
		actionSaveClose.setTitle("Save & Close");
		actionSaveClose.setIconEnabled(ImageLibrary.ICON_SAVECLOSE_ACTIVE);
		actionSaveClose.setIconDisabled(ImageLibrary.ICON_SAVECLOSE_INACTIVE);

		actionClose = new Action() {
			@Override
			public void run() {
				closeEditor();
			}
		};
		actionClose.setTitle("Close");
		actionClose.setIconEnabled(ImageLibrary.ICON_CLOSED);
		actionClose.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);

		actionEdit = new Action() {
			@Override
			public void run() {
				editEntity();
			}
		};
		actionEdit.setTitle("Edit");
		actionEdit.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		actionEdit.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
	}

	/**
	 * 
	 */
	protected void editEntity() {
		context.setAllEditable(true);
		actionSave.setEnabled(true);
		actionSaveClose.setEnabled(true);

		// for now consider that if we have edit rights, we open the entity always in edit mode (and never return from it)
		//actionEdit.setEnabled(false);
		actionEdit.setVisible(false);

	}

	/**
	 * 
	 */
	protected void closeEditor() {

		Site site = ExtendedApplication.getInstance(this).getSite();
		site.popPage(this);
		
	}

	/**
	 * 
	 */
	protected boolean performSave() {
		try {
			ValidationResult validationResult = context.saveToEntity();
			if (validationResult.hasErrors()) {
				getSessionContext().notifyMessage("The changes could not be saved due to validation errors. Please review the issues below and try again.", "error");
			} else {
				getSessionContext().notifyMessage("Your changes have been saved...");
				return true;
			}
		} catch (Exception e) {
			getSessionContext().notifyMessage("A Problem occured while saving your changes. (" + e + ")", "error");
			log.error("An error occured when saving an entity (" + context.getEntityDescriptor().getClassname() + ":#" + context.getInput().getEntity().getId() + ")", e);
		}
		return false;
	}

	/**
	 * 
	 */
	private void createToolbar() {
		
		toolbar = new ToolBar(this, "toolbar");
		ToolBarGroup grpDefault = toolbar.addGroup();

		grpDefault.addAction(actionClose);
		grpDefault.addAction(actionSaveClose);
		grpDefault.addAction(actionSave);
		grpDefault.addAction(actionEdit);
		
		
	}

	/**
	 * Create the editor.
	 * @param editorModel
	 */
	private void createContent(IEntity entity, EditorConfiguration editorConfig) {
		
		try {
			
			GenericEditorInput input = new GenericEditorInput(entity, editorConfig);
			editor = new EntityEditor(this, "editor", input);
			context = editor.getContext();
		
			if (context.isEditable()) {
				editEntity();
			} else {
				context.setAllEditable(false);
				actionSave.setEnabled(false);
				actionSaveClose.setEnabled(false);
			}
			
		} catch (Exception e) {
			log.error("Error creating editor", e);
			getSessionContext().notifyMessage("Error: " + e.toString());
		}
	}

	/**
	 * Returns the underlying EditorContext.
	 */
	public EditorContext getContext() {
		return context;
	}
}
