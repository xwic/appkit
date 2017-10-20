/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * com.netapp.pulse.start.ui.home.QuickLaunchUIItem
 */
package de.xwic.appkit.webbase.home;

import de.jwic.base.ImageRef;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * @author lippisch
 */
public class QuickLaunchUIItem {

	private String group = null;
	private String title = null;
	private String content = null;
	private ImageRef icon = null;
	private String reference = null;
	private long quickLinkId = 0;
	private int tempId = 0;
	private boolean mostCommon = false;

	/**
	 * @param sm
	 */
	public QuickLaunchUIItem(SubModule sm) {

		String qlTitle = sm.getFullTitle();
		if (qlTitle == null) {
			qlTitle = sm.getTitle();
		}
		setTitle(qlTitle);
		setContent(sm.getDescription());
		setIcon(sm.getIconLarge());
		setMostCommon(sm.isCommonModule());

	}

	/**
	 *
	 */
	public QuickLaunchUIItem() {
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the icon
	 */
	public ImageRef getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(ImageRef icon) {
		this.icon = icon;
	}
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	/**
	 * @return the quickLinkId
	 */
	public long getQuickLinkId() {
		return quickLinkId;
	}
	/**
	 * @param quickLinkId the quickLinkId to set
	 */
	public void setQuickLinkId(long quickLinkId) {
		this.quickLinkId = quickLinkId;
	}
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the tempId
	 */
	public int getTempId() {
		return tempId;
	}
	/**
	 * @param tempId the tempId to set
	 */
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	/**
	 * @return the mostCommon
	 */
	public boolean isMostCommon() {
		return mostCommon;
	}
	/**
	 * @param mostCommon the mostCommon to set
	 */
	public void setMostCommon(boolean mostCommon) {
		this.mostCommon = mostCommon;
	}




}
