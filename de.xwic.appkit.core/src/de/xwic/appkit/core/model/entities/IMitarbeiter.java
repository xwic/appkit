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
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Interface for the Mitarbeiter. <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface IMitarbeiter extends IEntity {
    
    /**
     * @return Returns the einheit.
     */
    public IPicklistEntry getEinheit();
    /**
     * @param einheit The einheit to set.
     */
    public void setEinheit(IPicklistEntry einheit);
    /**
     * @return Returns the email.
     */
    public String getEmail();
    /**
     * @param email The email to set.
     */
    public void setEmail(String email);
    /**
     * @return Returns the fax.
     */
    public String getFax();
    /**
     * @param fax The fax to set.
     */
    public void setFax(String fax);
    /**
     * @return Returns the handyNr.
     */
    public String getHandyNr();
    /**
     * @param handyNr The handyNr to set.
     */
    public void setHandyNr(String handyNr);
    /**
     * @return Returns the nachname.
     */
    public String getNachname();
    /**
     * @param nachname The nachname to set.
     */
    public void setNachname(String nachname);
    /**
     * @return Returns the salesTeam.
     */
    public ISalesTeam getSalesTeam();
    /**
     * @param salesTeam The salesTeam to set.
     */
    public void setSalesTeam(ISalesTeam salesTeam);
    /**
     * @return Returns the telefon.
     */
    public String getTelefon();
    /**
     * @param telefon The telefon to set.
     */
    public void setTelefon(String telefon);
    /**
     * @return Returns the vorname.
     */
    public String getVorname();
    /**
     * @param vorname The vorname to set.
     */
    public void setVorname(String vorname);
    /**
     * @return Returns the zusatz.
     */
    public String getZusatz();
    /**
     * @param zusatz The zusatz to set.
     */
    public void setZusatz(String zusatz);
    
    /**
	 * @return Returns the logonName.
	 */
	public String getLogonName();

	/**
	 * @param logonName The logonName to set.
	 */
	public void setLogonName(String logonName);

	/**
	 * Returns true if the mitarbeiter is "ausgeschieden".
	 */
	public boolean isAusgeschieden();
	
	/**
	 * Set to true if the mitarbeiter is ausgeschieden.
	 * @param ausgeschieden
	 */
	public void setAusgeschieden(boolean ausgeschieden);
	
	   /**
	 * @return the vorgesetzter
	 */
	public IMitarbeiter getVorgesetzter();

	/**
	 * @param vorgesetzter the vorgesetzter to set
	 */
	public void setVorgesetzter(IMitarbeiter vorgesetzter);
	
	/**
	 * @return the team
	 */
	public IPicklistEntry getTeam();

	/**
	 * @param team the team to set
	 */
	public void setTeam(IPicklistEntry team);
	
	/**
	 * @return the info1
	 */
	public boolean isInfo1();

	/**
	 * @param info1 the info1 to set
	 */
	public void setInfo1(boolean info1);
	
	/**
	 * @return the specialAccount
	 */
	public boolean isSpecialAccount();

	/**
	 * @param specialAccount the specialAccount to set
	 */
	public void setSpecialAccount(boolean specialAccount);
}
