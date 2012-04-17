/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.HistoryVersion
 * Created on Dec 15, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history;

import de.xwic.appkit.core.dao.IEntity;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class HistoryVersion {

	private IEntity historyEntity;
	private boolean changed;
	
	/**
	 * 
	 * @param hisVersion
	 * @param changed
	 */
	public HistoryVersion(IEntity hisEntity, boolean changed) {
		if (hisEntity == null) {
			throw new IllegalArgumentException("Parameter is null! ");
		}
		
		this.historyEntity = hisEntity;
		this.changed = changed;
	}


	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}


	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}


	/**
	 * @return the historyVersion
	 */
	public IEntity getHistoryEntity() {
		return historyEntity;
	}


	/**
	 * @param historyVersion the historyVersion to set
	 */
	public void setHistoryEntity(IEntity historyEntity) {
		this.historyEntity = historyEntity;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (changed ? 1231 : 1237);
		result = PRIME * result + ((historyEntity == null) ? 0 : historyEntity.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HistoryVersion other = (HistoryVersion) obj;
		if (changed != other.changed)
			return false;
		if (historyEntity == null) {
			if (other.historyEntity != null)
				return false;
		} else if (!historyEntity.equals(other.historyEntity))
			return false;
		return true;
	}
	
}
