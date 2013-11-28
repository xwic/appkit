/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.IRightDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;

/**
 * @author Florian Lippisch
 */
public interface IRightDAO extends DAO<IRight> {

	/**
	 * Returns the list of rights assigned to the specified role.
	 * @param role
	 * @return
	 */
	public EntityList getRightsByRole(IRole role);

	/**
	 * Create the right for the specified scope, action, role
	 * @param role
	 * @param scope
	 * @param action
	 * @return
	 */
	public IRight createRight(IRole role, IScope scope, IAction action);

}
