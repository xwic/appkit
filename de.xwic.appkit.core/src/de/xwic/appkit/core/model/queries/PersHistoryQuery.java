/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.PersHistoryQuery
 * Created on 20.09.2005
 *
 */
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

	private int entityID = 0;
	private int persHisTyp = 0;
	private int rolleID = -1;
	private int kontaktID = -1;
	private int mitarbeiterID = -1;
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
	public int getEntityID() {
		return entityID;
	}

	/**
	 * @param entityID The entityID to set.
	 */
	public void setEntityID(int entityID) {
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
	public int getKontaktID() {
		return kontaktID;
	}

	/**
	 * @param kontaktID The kontaktID to set.
	 */
	public void setKontaktID(int kontaktID) {
		this.kontaktID = kontaktID;
	}

	/**
	 * @return Returns the mitarbeiterID.
	 */
	public int getMitarbeiterID() {
		return mitarbeiterID;
	}

	/**
	 * @param mitarbeiterID The mitarbeiterID to set.
	 */
	public void setMitarbeiterID(int mitarbeiterID) {
		this.mitarbeiterID = mitarbeiterID;
	}

	/**
	 * @return Returns the rolleID.
	 */
	public int getRolleID() {
		return rolleID;
	}

	/**
	 * @param rolleID The rolleID to set.
	 */
	public void setRolleID(int rolleID) {
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
