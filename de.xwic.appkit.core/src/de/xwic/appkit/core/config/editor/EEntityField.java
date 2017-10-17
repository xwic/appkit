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
package de.xwic.appkit.core.config.editor;

/**
 * 
 * A field that handles entity references
 * 
 * @author Andra Iacovici
 * @editortag entity
 */
public class EEntityField extends EField {

	private String titlePattern = null;
	private String selectMessage = null;
	private String parameters = null;

	private boolean lifeSearch = true;
	private String sortBy = null;
	
	/**
	 * @return the titlePattern
	 */
	public String getTitlePattern() {
		return titlePattern;
	}

	/**
	 * Title pattern is used while displaying the entity title
	 * @param titlePattern the titlePattern to set
	 */
	public void setTitlePattern(String titlePattern) {
		this.titlePattern = titlePattern;
	}

	/**
	 * @return the selectMessage
	 */
	public String getSelectMessage() {
		return selectMessage;
	}

	/**
	 * @param selectMessage the selectMessage to set
	 */
	public void setSelectMessage(String selectMessage) {
		this.selectMessage = selectMessage;
	}

	
	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	
	/**
	 * Defines parameters for the search fields
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the lifeSearch
	 */
	public boolean isLifeSearch() {
		return lifeSearch;
	}

	/**
	 * @param lifeSearch the lifeSearch to set
	 */
	public void setLifeSearch(boolean lifeSearch) {
		this.lifeSearch = lifeSearch;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}
