/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.impl.Right
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;

/**
 * @author Florian Lippisch
 */
public class Right extends Entity implements IRight {

	private IRole role = null;
	private IScope scope = null;
	private IAction action = null;
	
	private int myHash = 0;
	private boolean hashCalculated = false;
	
	/**
	 * Default constructor.
	 *
	 */
	public Right() {
		
	}
	
	/**
	 * @param scope2
	 * @param action2
	 */
	public Right(IRole role, IScope scope, IAction action) {
		this.role = role;
		this.scope = scope;
		this.action = action;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#getRole()
	 */
	public IRole getRole() {
		return role;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#setRole(de.xwic.appkit.core.security.IRole)
	 */
	public void setRole(IRole role) {
		this.role = role;
		hashCalculated = false;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#getScope()
	 */
	public IScope getScope() {
		return scope;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#setScope(de.xwic.appkit.core.security.IScope)
	 */
	public void setScope(IScope scope) {
		this.scope = scope;
		hashCalculated = false;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#getAction()
	 */
	public IAction getAction() {
		return action;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IRight#setAction(de.xwic.appkit.core.security.IAction)
	 */
	public void setAction(IAction action) {
		this.action = action;
		hashCalculated = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "right '" + (action != null? action.getName() : "null") 
				+ "' on '" + (scope != null ? scope.getName() : "null" ) + "'"
				+ " for role '" + (role != null ? role.getName() : "null") + "'";
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
		
		Right r2 = (Right)obj;
		
		if (action != null) {
			if (r2.action == null || r2.action.getId() != action.getId()) {
				return false;
			}
		} else {
			if (r2.action != null) {
				return false;
			}
		}

		if (scope != null) {
			if (r2.scope == null || r2.scope.getId() != scope.getId()) {
				return false;
			}
		} else {
			if (r2.scope != null) {
				return false;
			}
		}

		if (role != null) {
			if (r2.role == null || r2.role.getId() != role.getId()) {
				return false;
			}
		} else {
			if (r2.role != null) {
				return false;
			}
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// I calculate the hashcode only once or on changes. 
		// If the ID of an referenced entity (action, role, scope) is 
		// changed, the recalc request will fail as it does not get noticed.
		// But since the ID usualy never changes, it is ok to do so.
		if (!hashCalculated) {
			myHash = 17;
			myHash = 37 * myHash + (action != null ? action.getId() : 0);
			myHash = 37 * myHash + (role != null ? role.getId() : 0);
			myHash = 37 * myHash + (scope != null ? scope.getId() : 0);
			hashCalculated = true;
		}
		return myHash;
	}
	
}
