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
package de.xwic.appkit.core.model.queries;

import java.util.Date;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * Query for PersonenHistory entity. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class PersHistoryQuery extends EntityQuery {

	private long entityID = 0L;
	private int persHisTyp = 0;
	private long rolleID = -1L;
	private long kontaktID = -1L;
	private long mitarbeiterID = -1L;
	private Date von = null;
	private Date bis = null;
	private boolean findLastOne = false;
	
	/**
	 * default constructor. <p>
	 */
	public PersHistoryQuery(){
	}

	/**
	 * @return Returns the entityID.
	 */
	public long getEntityID() {
		return entityID;
	}

	/**
	 * @param entityID The entityID to set.
	 */
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}

	/**
	 * @return Returns the persHisTyp.
	 */
	public int getPersHisTyp() {
		return persHisTyp;
	}

	/**
	 * @param persHisTyp The persHisTyp to set.
	 */
	public void setPersHisTyp(int persHisTyp) {
		this.persHisTyp = persHisTyp;
	}

	/**
	 * @return Returns the kontaktID.
	 */
	public long getKontaktID() {
		return kontaktID;
	}

	/**
	 * @param kontaktID The kontaktID to set.
	 */
	public void setKontaktID(long kontaktID) {
		this.kontaktID = kontaktID;
	}

	/**
	 * @return Returns the mitarbeiterID.
	 */
	public long getMitarbeiterID() {
		return mitarbeiterID;
	}

	/**
	 * @param mitarbeiterID The mitarbeiterID to set.
	 */
	public void setMitarbeiterID(long mitarbeiterID) {
		this.mitarbeiterID = mitarbeiterID;
	}

	/**
	 * @return Returns the rolleID.
	 */
	public long getRolleID() {
		return rolleID;
	}

	/**
	 * @param rolleID The rolleID to set.
	 */
	public void setRolleID(long rolleID) {
		this.rolleID = rolleID;
	}

	/**
	 * @return Returns the bis.
	 */
	public Date getBis() {
		return bis;
	}

	/**
	 * @param bis The bis to set.
	 */
	public void setBis(Date bis) {
		this.bis = bis;
	}

	/**
	 * @return Returns the von.
	 */
	public Date getVon() {
		return von;
	}

	/**
	 * @param von The von to set.
	 */
	public void setVon(Date von) {
		this.von = von;
	}

	/**
	 * @return Returns the findLastOne.
	 */
	public boolean isFindLastOne() {
		return findLastOne;
	}

	/**
	 * @param findLastOne The findLastOne to set.
	 */
	public void setFindLastOne(boolean findLastOne) {
		this.findLastOne = findLastOne;
	}
}
