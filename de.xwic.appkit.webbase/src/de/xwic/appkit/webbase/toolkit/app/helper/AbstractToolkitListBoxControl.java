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

import de.jwic.util.IHTMLElement;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public abstract class AbstractToolkitListBoxControl<E extends IEntity, A extends IEntityListBoxControl<E> & IHTMLElement> extends
	AbstractToolkitHTMLElementControl<A> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(A control, Object obj) {
		control.selectEntry((E) obj);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(A control) {
		return control.getSelectedEntry();
	}

}
