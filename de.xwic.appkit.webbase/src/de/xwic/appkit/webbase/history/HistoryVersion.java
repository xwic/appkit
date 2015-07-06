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
