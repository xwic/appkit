/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.IMitarbeiter
 * Created on 18.07.2005
 *
 */
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
