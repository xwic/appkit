/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.history.MitarbeiterHistory
 * Created on 19.12.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.entities.impl.history;

import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.impl.MitarbeiterBase;

/**
 * @author Florian Lippisch
 */
public class MitarbeiterHistory extends MitarbeiterBase implements IMitarbeiter, IHistory {

	private int historyReason = 0;
	private int entityID = 0;
	private long entityVersion = 0;
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IHistory#getHistoryReason()
	 */
	public int getHistoryReason() {
		return historyReason;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IHistory#setHistoryReason(int)
	 */
	public void setHistoryReason(int reason) {
		this.historyReason = reason;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IHistory#getEntityID()
	 */
	public int getEntityID() {
		return entityID;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IHistory#setEntityID(int)
	 */
	public void setEntityID(int id) {
		this.entityID = id;
	}

	/**
	 * @return Returns the entityVersion.
	 */
	public long getEntityVersion() {
		return entityVersion;
	}

	/**
	 * @param entityVersion The entityVersion to set.
	 */
	public void setEntityVersion(long entityVersion) {
		this.entityVersion = entityVersion;
	}


}
