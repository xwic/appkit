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
package de.xwic.appkit.core.security;

import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * A role defines a set of rights that can be assigned to one or more users.
 * @author Florian Lippisch
 */
public interface IRole extends IEntity {

	/**
	 * Returns the name of the role
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the name of the role.
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * @return the restrictGrantToPeers
	 */
	public Boolean getRestrictGrantToPeers();
	
	/**
	 * @param restrictGrantToPeers the restrictGrantToPeers to set
	 */
	public void setRestrictGrantToPeers(Boolean restrictGrantToPeers);
	
	/**
	 * @return the assignableRoles
	 */
	public Set<IRole> getAssignableRoles();

	/**
	 * @param assignableRoles the assignableRoles to set
	 */
	public void setAssignableRoles(Set<IRole> assignableRoles);
	
	/**
	 * 
	 * @param hidden
	 */
	public void setHidden(Boolean hidden);
	
	/**
	 * 
	 * @return
	 */
	public Boolean isHidden();
}
