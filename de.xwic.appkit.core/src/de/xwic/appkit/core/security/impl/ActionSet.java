/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.impl.ActionSet
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.impl;

import java.util.HashSet;
import java.util.Set;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IActionSet;

/**
 * @author Florian Lippisch
 */
public class ActionSet extends Entity implements IActionSet {

	private String name = null;
	private Set<IEntity> actions = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IActionSet#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IActionSet#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IActionSet#getActions()
	 */
	public Set<IEntity> getActions() {
		if (actions == null) {
			actions = new HashSet<IEntity>();
		}
		return actions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IActionSet#setActions(java.util.Set)
	 */
	public void setActions(Set<IEntity> actions) {
		this.actions = actions;
	}

}
