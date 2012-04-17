/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.connector.api.sync.ISynchronisable
 * Created on 30.11.2007 by Mark Frewin
 *
 */
package de.xwic.appkit.core.sync;

import java.util.Date;

/**
 * @author Mark Frewin
 */
public interface ISynchronizable {
		
	/**
	 * 
	 * @return
	 */
	public String getExternalId();
	
	/**
	 * Date time the item was last modified.
	 * @return
	 */
	public Date getLastModified();
	
}
