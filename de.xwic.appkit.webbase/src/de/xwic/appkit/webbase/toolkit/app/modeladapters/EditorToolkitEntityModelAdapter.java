/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.toolkit.app.modeladapters.EditorToolkitEntityModelAdapter 
 */

package de.xwic.appkit.webbase.toolkit.app.modeladapters;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Andrei Pat
 *
 */
public class EditorToolkitEntityModelAdapter extends EditorToolkitBeanModelAdapter {

	/**
	 * @param modelBean
	 */
	public EditorToolkitEntityModelAdapter(IEntity modelBean) {
		super(modelBean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.modeladapters.EditorToolkitBeanModelAdapter#getClazz()
	 */
	@Override
	protected Class getBeanClazz() {
		return ((IEntity) modelBean).type();
	}
}
