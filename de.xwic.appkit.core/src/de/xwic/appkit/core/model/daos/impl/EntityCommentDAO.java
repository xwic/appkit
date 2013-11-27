/*
 * (c) Copyright 2005, 2006 by pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.EntityCommentDAO
 * Created on Jan 8, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.core.model.entities.impl.EntityComment;


/**
 *
 *
 * @author Aron Cotrau
 */
public class EntityCommentDAO extends AbstractDAO<IEntityComment, EntityComment> implements IEntityCommentDAO {

	/**
	 *
	 */
	public EntityCommentDAO() {
		super(IEntityComment.class, EntityComment.class);
	}

}
