/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.ql.IQuickLaunchFunction 
 */
package de.xwic.appkit.webbase.home.ql;

import de.jwic.base.ControlContainer;
import de.jwic.base.ImageRef;
import de.xwic.appkit.core.security.IUser;


/**
 * @author Alex Cioroianu
 *
 */
public interface IQuickLaunchFunction {
	
	/**
	 * @return
	 */
	public String getTitle();
	
	/**
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * @return
	 */
	public String getDescription() ;
	
	/**
	 * @param description
	 */
	public void setDescription(String description) ;	

	/**
	 * @param icon
	 */
	public void setIcon(ImageRef icon) ;
	
	/**
	 * @return
	 */
	public ImageRef getIcon();

	/**
	 * @param icon
	 */
	public void setReference(String icon) ;
	
	/**
	 * @return
	 */
	public String getReference();
	
	/**
	 * @param quickLaunchPanel
	 */
	public void run(ControlContainer quickLaunchPanel);
	
	/**
	 * @param user
	 * @return
	 */
	boolean isAvailable(IUser user);
	
	/**
	 * @return the defaultVisible
	 */
	public boolean isDefaultVisible();

	
	/**
	 * @param defaultVisible the defaultVisible to set
	 */
	public void setDefaultVisible(boolean defaultVisible);

	
}
