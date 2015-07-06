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
