/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.ql.QuickLaunchConfigDialog 
 */
package de.xwic.appkit.webbase.home.ql;

import de.jwic.base.Dimension;
import de.jwic.base.Page;
import de.jwic.controls.ScrollableContainer;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.home.QuickLaunchModel;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author lippisch
 *
 */
public class QuickLaunchConfigDialog extends AbstractDialogWindow {

	private QuickLaunchModel model;
	private String appId;
	
	/**
	 * @param site
	 * @param model 
	 */
	public QuickLaunchConfigDialog(Site site, QuickLaunchModel model, String appId) {
		super(site);
		this.model = model;
		this.appId = appId;
		
		setCloseable(true);
		setModal(true);
		setTitle("Quick Launch");
		
		Page page = Page.findPage(site);
		Dimension pageSize = page.getPageSize();
		setWidth(600);
		setHeight((int)(pageSize.height * 0.8d));
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {

		ScrollableContainer cnt = new ScrollableContainer(content);
		cnt.setWidth("100%");
		cnt.setHeight((getHeight() - 50) + "px");
		new QuickLaunchSelector(cnt, "qlSel", model, appId);
		
	}
	
}
