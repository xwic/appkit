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

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Label;
import de.xwic.appkit.core.config.editor.EPicklistCheckbox;
import de.xwic.appkit.core.config.editor.EUserRoles;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.controls.UserRolesEditorControl;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.mappers.PicklistEntrySetMapper;
import de.xwic.appkit.webbase.editors.mappers.UserRolesMapper;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl;

/**
 * The Builder for a multi-value PicklistCheckboxControl.
 * 
 * @author lippisch
 */
public class EUserRolesBuilder extends Builder<EUserRoles> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
	 *      de.jwic.base.IControlContainer,
	 *      de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	public IControl buildComponents(EUserRoles eUR, IControlContainer parent, IBuilderContext context) {

		if (eUR.getProperty() != null) {
			UserRolesEditorControl urEditor = new UserRolesEditorControl(parent, null);
			urEditor.setColumns(eUR.getCols());
			
			context.registerField(eUR.getProperty(), urEditor, eUR, UserRolesMapper.MAPPER_ID);
			
			if (context instanceof EditorContext) {
				EditorContext ex = (EditorContext)context;
				ex.addEditorListener(new EditorAdapter() {
					@Override
					public void afterSave(EditorEvent event) {
						urEditor.saveRoles();
					}
				});
			}

			return urEditor;
			
		} else {

			Label label = null;
			label = new Label(parent);
			label.setText("You need to specify a property that contains the logonName");
			return label;
			
		}

	}
}
