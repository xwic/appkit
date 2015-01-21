/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.LangKey
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.model.daos;

import java.io.Serializable;

/**
 * Helperclass for temporarely IPicklistText entries.
 * <p>
 * 
 * @author Ronny Pfretzschner
 */
public class LangKey implements Serializable {

	private int hashCode = 0;

	private boolean hashDone = false;

	private int id;

	private String langID;

	/**
	 * constructor
	 * 
	 * @param entryID
	 * @param langID
	 */
	public LangKey(int entryID, String langID) {
		this.id = entryID;
		this.langID = langID;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the langID.
	 */
	public String getLangID() {
		return langID;
	}

	/**
	 * @param langID
	 *            The langID to set.
	 */
	public void setLangID(String langID) {
		this.langID = langID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		LangKey key = (LangKey) obj;

		if (key.getId() == getId() && key.getLangID().equalsIgnoreCase(getLangID())) {

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		if (!hashDone) {
			hashCode = 17;
			int multiplier = 59;

			hashCode = hashCode * multiplier + (langID != null ? langID.toLowerCase().hashCode() : 0);
			hashCode = hashCode * multiplier + id;
			hashDone = true;
		}
		return hashCode;
	}
}
