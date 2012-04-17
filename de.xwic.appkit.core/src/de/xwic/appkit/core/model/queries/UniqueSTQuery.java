/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.UniqueSTQuery
 * Created on 05.08.2005
 *
 */
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.ISalesTeam;

/**
 * Check, if salesteam is unique. <p>
 * 
 * @author Ronny Pfretzschner
 */
public class UniqueSTQuery extends EntityQuery {

	private ISalesTeam st = null;
	
	/**
	 * default ctor. <p>
	 * 
	 * just for webservice!
	 */
	public UniqueSTQuery(){
	}
	
	/**
	 * 
	 * @param the salesteam to be checked
	 */
	public UniqueSTQuery(ISalesTeam st){
		if (st == null) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
		}
		
		this.st = st;
	}

	/**
	 * @return Returns the st.
	 */
	public ISalesTeam getSt() {
		return st;
	}

	/**
	 * @param st The st to set.
	 */
	public void setSt(ISalesTeam st) {
		if (st == null) {
            throw new IllegalArgumentException("Error in setter in class: " + getClass().getName() + "! Arguments not allowed!");
		}
		this.st = st;
	}

	
	
}
