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

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.ISalesTeam;

/**
 * Query for the Short Filter option in the common Mitarbeiter view. <p>
 * 
 * Constructor should be used for searchpane search. <p>
 * Use default and / or setter of other properties to get combined AND result.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class CMFastSearchQuery extends EntityQuery {
	
    private int svID = -1;
    private int dokID = -1;

	private int einheitPeID = -1;
	private String nachname = null;
	private String vorname = null;
	private int salesTeamID = -1;
	private String logonName = null;

	/**
	 * default constructor. <p>
	 */
	public CMFastSearchQuery() {
	}
	
	/**
	 * Constructor. <p>
	 * 
	 * Searching values null are ignored within the search. <p>
	 * 
	 * So if you want to ignore filter options, set them just to null.
	 * 
	 * @param nachname
	 * @param einheit
	 * @param team
	 */
	public CMFastSearchQuery(String nachname, IPicklistEntry einheit, ISalesTeam team, String logonName) {
		this.einheitPeID = einheit != null ? einheit.getId() : -1;
		this.nachname = nachname;
		this.salesTeamID  = team != null ? team.getId() : -1;
		this.logonName = logonName;
	}

	/**
	 * @return Returns the einheitPeID.
	 */
	public int getEinheitPeID() {
		return einheitPeID;
	}

	/**
	 * @param einheitPeID The einheitPeID to set.
	 */
	public void setEinheitPeID(int einheitPeID) {
		this.einheitPeID = einheitPeID;
	}

	/**
	 * @return Returns the logonName.
	 */
	public String getLogonName() {
		return logonName;
	}

	/**
	 * @param logonName The logonName to set.
	 */
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}

	/**
	 * @return Returns the nachname.
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * @param nachname The nachname to set.
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	/**
	 * @return Returns the salesTeamID.
	 */
	public int getSalesTeamID() {
		return salesTeamID;
	}

	/**
	 * @param salesTeamID The salesTeamID to set.
	 */
	public void setSalesTeamID(int salesTeamID) {
		this.salesTeamID = salesTeamID;
	}

	/**
	 * @return Returns the vorname.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * @param vorname The vorname to set.
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * @return the dokID
	 */
	public int getDokID() {
		return dokID;
	}

	/**
	 * @param dokID the dokID to set
	 */
	public void setDokID(int dokID) {
		this.dokID = dokID;
	}

	/**
	 * @return the svID
	 */
	public int getSvID() {
		return svID;
	}

	/**
	 * @param svID the svID to set
	 */
	public void setSvID(int svID) {
		this.svID = svID;
	}
}
