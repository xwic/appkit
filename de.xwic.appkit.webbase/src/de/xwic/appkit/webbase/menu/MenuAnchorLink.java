/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * com.netapp.commons.menu.MenuAnchorLink
 */

package de.xwic.appkit.webbase.menu;

import de.jwic.base.IControlContainer;
import de.jwic.controls.AnchorLink;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author bogdan
 *
 */
public class MenuAnchorLink extends AnchorLink {

	public MenuAnchorLink(IControlContainer container, String module, String subModule) {
		this(container, null, module, subModule);
	}

	public MenuAnchorLink(IControlContainer container, String name, final String module, final String subModule) {
		super(container, name);
		final Site site = ExtendedApplication.getSite(getSessionContext());
		this.setTemplateName(AnchorLink.class.getName());
		this.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				site.actionSelectMenu(module + ";" + subModule);
			}
		});
	}

}
