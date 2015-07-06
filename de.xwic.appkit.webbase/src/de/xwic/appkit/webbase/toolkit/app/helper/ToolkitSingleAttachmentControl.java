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

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.attachment.SingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitSingleAttachmentControl implements IToolkitControlHelper<SingleAttachmentControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	@Override
	public SingleAttachmentControl create(IControlContainer container, String name,
			Object optionalParam) {
		return new SingleAttachmentControl(container, name, (EditorModel) optionalParam);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(SingleAttachmentControl control) {
		IAnhang value = null;
		IAttachmentWrapper wrapper = control.saveAttachment();
		if (wrapper != null && !wrapper.isDeleted()) {
			value = wrapper.getAnhang();
		}

		return value;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(SingleAttachmentControl control) {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(SingleAttachmentControl control, Object obj) {
		control.loadAttachment((IAnhang) obj);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(SingleAttachmentControl control, String cssClass) {

	}

}
