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
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.Set;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryMultiSelectControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public class ToolkitPicklistSelectionMultiControl extends AbstractToolkitHTMLElementControl<PicklistEntryMultiSelectControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public PicklistEntryMultiSelectControl create(IControlContainer container, String name, Object optionalParam) {
		return new PicklistEntryMultiSelectControl(container, name, (String) optionalParam);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(PicklistEntryMultiSelectControl control) {
		return control.getSelectedEntries();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(PicklistEntryMultiSelectControl control, Object obj) {
		control.selectEntries((Set<IPicklistEntry>) obj);
	}
}
