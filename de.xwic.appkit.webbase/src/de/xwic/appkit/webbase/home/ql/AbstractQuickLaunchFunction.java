/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.ql.AbstractQuickLaunchFunction 
 */
package de.xwic.appkit.webbase.home.ql;

import de.jwic.base.ImageRef;


/**
 * @author Alex Cioroianu
 *
 */
public abstract class AbstractQuickLaunchFunction implements IQuickLaunchFunction{
	
	private String title;
	private String description;
	private ImageRef icon;
	private String reference;
	private boolean defaultVisible = false;
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#setIcon(de.jwic.base.ImageRef)
	 */
	@Override
	public void setIcon(ImageRef icon) {
		this.icon = icon;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#getIcon()
	 */
	@Override
	public ImageRef getIcon() {
		return this.icon;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#setReference(java.lang.String)
	 */
	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.IQuickLaunchFunction#getReference()
	 */
	@Override
	public String getReference() {
		return reference;
	}

	
	/**
	 * @return the defaultVisible
	 */
	@Override
	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	
	/**
	 * @param defaultVisible the defaultVisible to set
	 */
	@Override
	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

}
