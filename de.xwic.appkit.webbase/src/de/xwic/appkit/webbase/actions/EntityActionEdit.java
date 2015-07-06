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
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.webbase.actions.editors.EditorHelper;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
public class EntityActionEdit extends AbstractEntityAction {

	/**
	 * @param site
	 * @param entityDao
	 * @param entityProvider
	 */
	public EntityActionEdit(Site site, DAO entityDao, IEntityProvider entityProvider) {
		setTitle("Edit");
		setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
		setVisible(true);
		setEnabled(false);
		
		this.site = site;
		this.entityDao = entityDao;
		this.entityProvider = entityProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			EditorHelper.getInstance().openEditor(site, entityProvider.getEntity(), entityProvider.getBaseEntity());
		} catch (Exception e) {
			log.error("Error while executing EntityActionEdit", e);
			throw new RuntimeException(e);
		}
	}
}