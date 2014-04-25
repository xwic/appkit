/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.ActionSetDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.impl.ActionSet;

/**
 * @author Florian Lippisch
 */
public class ActionSetDAO extends AbstractDAO<IActionSet, ActionSet> implements IActionSetDAO {

	/**
	 *
	 */
	public ActionSetDAO() {
		super(IActionSet.class, ActionSet.class);
	}

}
