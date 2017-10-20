/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.HomePage 
 */
package de.xwic.appkit.webbase.home;

import java.io.IOException;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.Label;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.IPageControl;
import de.xwic.appkit.webbase.utils.NotificationUtil;

/**
 * Content of the Get Started page.
 * @author lippisch
 */
public class HomePage extends ControlContainer implements IPageControl {

	private CheckBox ckRestore;
	

	/**
	 * Constructs a new home page. 
	 * 
	 * @param container
	 * @param name
	 */
	public HomePage(IControlContainer container, String name) {
		this(container, name, null);				
	}
	
	/**
	 * 
	 * @param container
	 * @param name
	 * @param appId			application ID used in News and Launch cards.
	 */
	public HomePage(IControlContainer container, String name, String appId) {
		super(container, name);
		
		Label lbWelcome = new Label(this, "lbWelcome");
		new QuickLaunchPanel(this, "qlPanel", appId);
		new NewsFeed(this, "newsFeed", appId);
		
		// initialize
		IMitarbeiter ma =  DAOSystem.getDAO(IMitarbeiterDAO.class).getByCurrentUser();
		if (ma != null) {
			lbWelcome.setText("Welcome " + ma.getVorname());
		} else {
			lbWelcome.setText("Welcome [Unknown]");
		}
		
		IPreferenceStore prefStore = ExtendedApplication.getInstance(this).getSite().getPreferenceStore();
		ckRestore = new CheckBox(this,  "ckRestore");
		ckRestore.setLabel("Always open this page when starting Pulse");
		ckRestore.setChecked(!"true".equals(prefStore.getString("restoreNavigation", "false")));
		ckRestore.setInfoText("If you disable this option, Pulse will open the module you have activated the last time.");
		ckRestore.addValueChangedListener(new ValueChangedListener() {
			@Override
			public void valueChanged(ValueChangedEvent event) {
				toggleRestorePreference();
			}
		});
		
		
	}

	/**
	 * 
	 */
	protected void toggleRestorePreference() {
		IPreferenceStore prefStore = ExtendedApplication.getInstance(this).getSite().getPreferenceStore();
		prefStore.setValue("restoreNavigation", ckRestore.isChecked() ? "false" : "true");
		try {
			prefStore.flush();
			NotificationUtil.showInfo("Your preferences have been updated.", getSessionContext());
		} catch (IOException e) {
			log.error("Error flushing settings", e);
			NotificationUtil.showError("Error storing changes : "  + e, getSessionContext());
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IPageControl#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		throw new UnsupportedOperationException("Not supported.");
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IPageControl#getTitle()
	 */
	@Override
	public String getTitle() {
		return "Home";
	}

}
