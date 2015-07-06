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
package de.xwic.appkit.core.dao;

/**
 * Marks an entity as history object. The interfaces defines two properties used to 
 * identify the original object.
 * @author Florian Lippisch
 */
public interface IHistory extends IEntity {

	/**
	 * reason created code
	 */
	public final static int REASON_CREATED = 0;
	/**
	 * reason updated code
	 */
	public final static int REASON_UPDATED = 1;
	/**
	 * reason deleted code
	 */
	public final static int REASON_DELETED = 2;
	
	/**
	 * Returns the reason for this history object. A reason can be
	 * either REASON_CREATED (0), REASON_UPDATED (1) or 
	 * REASON_DELETED (2).
	 * @return
	 */
	public int getHistoryReason();
	
	/**
	 * Set the reason for the creation of this history object.
	 * @param reason
	 */
	public void setHistoryReason(int reason);
	
	/**
	 * Returns the ID of the object this history object belongs to.
	 * @return
	 */
	public int getEntityID();
	
	/**
	 * Sets the ID of the object this history object belongs to.
	 * @param id
	 */
	public void setEntityID(int id);
	
	/**
	 * Returns the version number of the entity.
	 * @return
	 */
	public long getEntityVersion();
	
	/**
	 * Sets the version number of the entity.
	 * @param entityVersion
	 */
	public void setEntityVersion(long entityVersion);
	
}
