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
package de.xwic.appkit.core.model.entities.impl.history;

import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.impl.MitarbeiterBase;

/**
 * @author Florian Lippisch
 */
public class MitarbeiterHistory extends MitarbeiterBase implements IMitarbeiter, IHistory {

	private int historyReason = 0;
	private long entityID = 0L;
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
	public long getEntityID() {
		return entityID;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IHistory#setEntityID(int)
	 */
	public void setEntityID(long id) {
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
