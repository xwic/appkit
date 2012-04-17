/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.IReportTemplate
 * Created on Dec 7, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;


/**
 * interface for the ReportTemplate entity.
 *
 * @author Aron Cotrau
 */
public interface IReportTemplate extends IEntity {

	/** picklist id for type */
	public static final String PL_TYPE = "rt.type";
	/** The entry key for details type */
	public static final String PE_TYPE_DETAILS = "details";
	/** The entry key for context type */
	public static final String PE_TYPE_CONTEXT = "context";
	/** The entry key for context type */
	public static final String PE_TYPE_EXTERNAL = "external";
	
	/**
	 * @return the contextType
	 */
	public String getContextType();
	
	/**
	 * @param contextType the contextType to set
	 */
	public void setContextType(String contextType);
	
	/**
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description);
	
	/**
	 * @return the scope
	 */
	public String getScope();
	
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope);
	
	/**
	 * @return the templateCode
	 */
	public String getTemplateCode();
	
	/**
	 * @param templateCode the templateCode to set
	 */
	public void setTemplateCode(String templateCode);
	
	/**
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title);
	
	/**
	 * @return the type
	 */
	public IPicklistEntry getType();
	
	/**
	 * @param type the type to set
	 */
	public void setType(IPicklistEntry type);	
	
	/**
	 * @return the folder
	 */
	public IReportFolder getFolder();

	
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(IReportFolder folder);
	
	/**
	 * @return the contentProviderId
	 */
	public String getContentProviderId();

	/**
	 * @param contentProviderId the contentProviderId to set
	 */
	public void setContentProviderId(String contentProviderId);

	/**
	 * @return the hidden
	 */
	public boolean isHidden();

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden);

	/**
	 * @return the referenceId
	 */
	public String getReferenceId();


	/**
	 * @param referenceId the referenceId to set
	 */
	public void setReferenceId(String referenceId);

}
