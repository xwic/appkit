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

import de.jwic.base.IControlContainer;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.actions.Action;
import de.jwic.controls.actions.IAction;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Provides a standard Page 'frame' for the EntityEditor.
 * @author Lippisch
 */
public class EntityEditorPage extends InnerPage {

	private EntityEditor editor;
	private EditorContext context;

	private IAction actionSave;
	private IAction actionSaveClose;
	private IAction actionClose;
	
	private IAction actionEdit;

	/**
	 * @param container
	 * @param name
	 */
	public EntityEditorPage(IControlContainer container, String name, IEntity entity, EditorConfiguration editorConfig) {
		super(container, name);
		
		setTitle("Experimental Editor");
		
		createActions();
		createToolbar();
		createContent(entity, editorConfig);
		
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
		actionEdit.setEnabled(false);

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
				getSessionContext().notifyMessage("There have been issues... *eek*", "error");
			} else {
				getSessionContext().notifyMessage("Your changes have been saved...");
				return true;
			}
		} catch (Exception e) {
			log.error("An error occured when saving an entity (" + context.getEntityDescriptor().getClassname() + ":#" + context.getInput().getEntity().getId() + ")", e);
		}
		return false;
	}

	/**
	 * 
	 */
	private void createToolbar() {
		
		ToolBar toolbar = new ToolBar(this, "toolbar");
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
		
			// start with all fields non-editable
			if (entity.getId() != 0) { // is not new
				context.setAllEditable(false);
				actionSave.setEnabled(false);
				actionSaveClose.setEnabled(false);
			} else {
				actionEdit.setEnabled(false);
			}
			
		} catch (Exception e) {
			log.error("Error creating editor", e);
			getSessionContext().notifyMessage("Error: " + e.toString());
		}
		
	}

	
}
