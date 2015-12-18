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
package de.xwic.appkit.core.security.impl;

import java.util.Set;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.security.IRole;

/**
 * @author Florian Lippisch
 */
public class Role extends Entity implements IRole {

	private String name = null;
	private Boolean restrictGrantToPeers;
	private Boolean hidden;
	private Set<IRole> assignableRoles;

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#getRestrictGrantToPeers()
	 */
	@Override
	public Boolean getRestrictGrantToPeers() {
		return restrictGrantToPeers;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#setRestrictGrantToPeers(java.lang.Boolean)
	 */
	@Override
	public void setRestrictGrantToPeers(Boolean restrictGrantToPeers) {
		this.restrictGrantToPeers = restrictGrantToPeers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "role: " + name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((restrictGrantToPeers == null) ? 0 : restrictGrantToPeers.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#getAssignableRoles()
	 */
	@Override
	public Set<IRole> getAssignableRoles() {
		return assignableRoles;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#setAssignableRoles(java.util.Set)
	 */
	@Override
	public void setAssignableRoles(Set<IRole> assignableRoles) {
		this.assignableRoles = assignableRoles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (restrictGrantToPeers == null) {
			if (other.restrictGrantToPeers != null) {
				return false;
			}
		} else if (!restrictGrantToPeers.equals(other.restrictGrantToPeers)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#setHidden(boolean)
	 */
	@Override
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#isHidden()
	 */
	@Override
	public Boolean isHidden() {
		return hidden;
	}
}
