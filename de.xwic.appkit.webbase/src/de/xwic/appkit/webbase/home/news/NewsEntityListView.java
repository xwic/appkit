/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.news.NewTableView 
 */
package de.xwic.appkit.webbase.home.news;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.model.entities.INews;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;

/**
 * @author lippisch
 *
 */
public class NewsEntityListView extends EntityListView<INews> {

	/**
	 * @param container
	 * @param name
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public NewsEntityListView(IControlContainer container, String name, EntityListViewConfiguration configuration)
			throws ConfigurationException {
		super(container, name, configuration);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.entityviewer.EntityListView#init()
	 */
	@Override
	protected void init() {
		super.init();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.entityviewer.EntityListView#addStandardActions()
	 */
	@Override
	protected void addStandardActions() {
		// no standard actions defined for News
	}
	
}
