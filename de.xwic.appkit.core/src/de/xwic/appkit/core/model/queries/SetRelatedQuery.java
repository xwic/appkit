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
package de.xwic.appkit.core.model.queries;


/**
 * @author Ronny Pfretzschner
 *
 */
public class SetRelatedQuery extends PropertyQuery {
	
	private String setProperty = null;
	private int entityID = -1;
	private String relatedEntityImplClazzName = null;
	
	/**
	 * Constructor.
	 *
	 */
	public SetRelatedQuery() {
		
	}
	
	/**
	 * default ctor. <p>
	 *
	 */
	public SetRelatedQuery(String wildCardPreferenceSetting) {
		super(wildCardPreferenceSetting);
	}


	/**
	 * @return the entityID
	 */
	public int getEntityID() {
		return entityID;
	}


	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}


	/**
	 * @return the setProperty
	 */
	public String getSetProperty() {
		return setProperty;
	}


	/**
	 * @param setProperty the setProperty to set
	 */
	public void setSetProperty(String setProperty) {
		this.setProperty = setProperty;
	}


	/**
	 * @return the relatedEntityClazzName
	 */
	public String getRelatedEntityImplClazzName() {
		return relatedEntityImplClazzName;
	}


	/**
	 * @param relatedEntityClazzName the relatedEntityClazzName to set
	 */
	public void setRelatedEntityImplClazzName(String relatedEntityImplClazzName) {
		this.relatedEntityImplClazzName = relatedEntityImplClazzName;
	}
	
	
}
