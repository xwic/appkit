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
/**
 * 
 */
package de.xwic.appkit.core.registry.impl;

import de.xwic.appkit.core.registry.IExtensionPoint;

/**
 * @author Adrian Ionescu
 */
public class DefaultExtensionPoint implements IExtensionPoint {

	private String id;
	private String description;
	private String documentationLink;
	
	/**
	 * 
	 */
	public DefaultExtensionPoint(String id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public DefaultExtensionPoint(String id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * 
	 */
	public DefaultExtensionPoint(String id, String description, String docuLink) {
		this.id = id;
		this.description = description;
		this.documentationLink = docuLink;
		
	}
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtensionPoint#getId()
	 */
	@Override
	public String getId() {
		return id;
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
	 * @return the documentationLink
	 */
	public String getDocumentationLink() {
		return documentationLink;
	}

	/**
	 * @param documentationLink the documentationLink to set
	 */
	public void setDocumentationLink(String documentationLink) {
		this.documentationLink = documentationLink;
	}
}
