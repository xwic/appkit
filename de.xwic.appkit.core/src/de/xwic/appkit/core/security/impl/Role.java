/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.impl.Role
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.security.IRole;

/**
 * @author Florian Lippisch
 */
public class Role extends Entity implements IRole {

	private String name = null;
	
	private int myHash = 0;
	private boolean hashCalculated = false;

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRole#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "role: " + name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (!obj.getClass().equals(getClass())) {
			return false;
		}
		
		Role role = (Role)obj;
		if (name == null) {
			if (role.name != null) {
				return false;
			}
			return getId() == role.getId();
		} else {
			return getId() == role.getId() && name.equals(role.name);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (!hashCalculated) {
			myHash = 17;
			myHash = 37 * myHash + (name != null ? name.hashCode() : 0);
			myHash = 37 * myHash + getId();
			hashCalculated = true;
		}
		return myHash;
	}

	
}
