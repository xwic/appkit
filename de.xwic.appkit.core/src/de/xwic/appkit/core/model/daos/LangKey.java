/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.model.daos;

/**
 * Helperclass for temporarely IPicklistText entries.
 * <p>
 * 
 * @author Ronny Pfretzschner
 */
public class LangKey {

	private int hashCode = 0;

	private boolean hashDone = false;

	private long id;

	private String langID;

	/**
	 * constructor
	 * 
	 * @param entryID
	 * @param langID
	 */
	public LangKey(long entryID, String langID) {
		this.id = entryID;
		this.langID = langID;
	}

	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(long id) {
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
			hashCode = hashCode * multiplier + (int) (id ^ (id >>> 32));
			hashDone = true;
		}
		return hashCode;
	}
	
}
