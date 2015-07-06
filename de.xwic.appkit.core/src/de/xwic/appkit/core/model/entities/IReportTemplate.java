/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
