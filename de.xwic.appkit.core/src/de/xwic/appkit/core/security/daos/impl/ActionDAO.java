/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.ActionDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.impl.Action;

/**
 * @author Florian Lippisch
 */
public class ActionDAO extends AbstractDAO<IAction, Action> implements IActionDAO {

	/**
	 *
	 */
	public ActionDAO() {
		super(IAction.class, Action.class);
	}

}
