/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.ISalesTeam
 * Created on 18.07.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Interface for the Sales Team Business Object. <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface ISalesTeam extends IEntity {
    /**
     * @return Returns the bezeichnung.
     */
    public String getBezeichnung();
    /**
     * @param bezeichnung The bezeichnung to set.
     */
    public void setBezeichnung(String bezeichnung);

}
