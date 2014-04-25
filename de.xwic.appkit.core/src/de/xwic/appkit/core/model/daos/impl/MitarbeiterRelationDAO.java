/*
 * (c) Copyright 2005, 2006 by pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.MitarbeiterRelationDAO
 * Created on Aug 6, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.daos.IMitarbeiterRelationDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiterRelation;
import de.xwic.appkit.core.model.entities.impl.MitarbeiterRelation;


/**
 *
 *
 * @author Aron Cotrau
 */
public class MitarbeiterRelationDAO extends AbstractDAO<IMitarbeiterRelation, MitarbeiterRelation> implements IMitarbeiterRelationDAO {

	/**
	 *
	 */
	public MitarbeiterRelationDAO() {
		super(IMitarbeiterRelation.class, MitarbeiterRelation.class);
	}

}
