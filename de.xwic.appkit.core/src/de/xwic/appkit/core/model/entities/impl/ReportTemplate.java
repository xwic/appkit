/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.ReportTemplate
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IReportFolder;
import de.xwic.appkit.core.model.entities.IReportTemplate;


/**
 * Implementation of the ReportTemplate entity.
 *
 * @author Aron Cotrau
 */
public class ReportTemplate extends Entity implements IReportTemplate {

	private String referenceId = null; 
	private String title = null; 
	private String scope = null; 
	private String templateCode = null; 
	private String description = null; 
	private String contextType = null;
	private String contentProviderId = null;
	private boolean hidden = false;
	
	private IPicklistEntry type = null;
	private IReportFolder folder = null;

	
	/**
	 * @return the contextType
	 */
	public String getContextType() {
		return contextType;
	}

	
	/**
	 * @param contextType the contextType to set
	 */
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	
	/**
	 * @return the templateCode
	 */
	public String getTemplateCode() {
		return templateCode;
	}

	
	/**
	 * @param templateCode the templateCode to set
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
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
	 * @return the type
	 */
	public IPicklistEntry getType() {
		return type;
	}

	
	/**
	 * @param type the type to set
	 */
	public void setType(IPicklistEntry type) {
		this.type = type;
	}


	
	/**
	 * @return the folder
	 */
	public IReportFolder getFolder() {
		return folder;
	}


	
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(IReportFolder folder) {
		this.folder = folder;
	}


	/**
	 * @return the contentProviderId
	 */
	public String getContentProviderId() {
		return contentProviderId;
	}


	/**
	 * @param contentProviderId the contentProviderId to set
	 */
	public void setContentProviderId(String contentProviderId) {
		this.contentProviderId = contentProviderId;
	}


	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}


	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}


	/**
	 * @return the referenceId
	 */
	public String getReferenceId() {
		return referenceId;
	}


	/**
	 * @param referenceId the referenceId to set
	 */
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
}
