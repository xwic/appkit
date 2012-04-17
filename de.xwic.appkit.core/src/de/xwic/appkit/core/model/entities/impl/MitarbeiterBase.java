/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.entities.impl.Mitarbeiter
 * Created on 18.07.2005
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.ISalesTeam;

/**
 * Implementation for the Mitarbeiter Business Object. <p>
 * 
 * @author Ronny Pfretzschner
 */
public class MitarbeiterBase extends Entity implements
        IMitarbeiter {

    private String vorname = "";
    private String nachname = "";
    private String zusatz = "";
    private String telefon = "";
    private String fax = "";
    private String handyNr = "";
    private String email = "";
    private IPicklistEntry einheit = null;
    private ISalesTeam salesTeam = null;
    
    private IPicklistEntry team = null;
    
    private String logonName = "";
    private boolean ausgeschieden = false;

    private IMitarbeiter vorgesetzter;

    private boolean info1 = false;
    
 	/**
     * default constructor. <p>
	 * Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
     */
    public MitarbeiterBase(){
    }

    /**
	 * Constructor for Mitarbeiter. <p>
	 * 
	 * Use this Constructor instead of the default!
     * 
     * @param vorname 
     * @param nachname
     * @param einheit the new Einheit 
     * @param team the SalesTeam, this Mitarbeiter belongs to
     */
    public MitarbeiterBase(String vorname,
            String nachname, IPicklistEntry einheit, ISalesTeam team) {

        if (nachname == null || nachname.length() < 1 ||
            vorname == null || vorname.length() < 1 ||
            team == null || einheit == null) {

            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        
        this.nachname = nachname;
        this.vorname = vorname;
        this.salesTeam = team;
        this.einheit = einheit;
    }
    
    /**
     * @return Returns the einheit.
     */
    public IPicklistEntry getEinheit() {
        return einheit;
    }
    /**
     * @param einheit The einheit to set.
     */
    public void setEinheit(IPicklistEntry einheit) {
        this.einheit = einheit;
    }
    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return Returns the fax.
     */
    public String getFax() {
        return fax;
    }
    /**
     * @param fax The fax to set.
     */
    public void setFax(String fax) {
        this.fax = fax;
    }
    /**
     * @return Returns the handyNr.
     */
    public String getHandyNr() {
        return handyNr;
    }
    /**
     * @param handyNr The handyNr to set.
     */
    public void setHandyNr(String handyNr) {
        this.handyNr = handyNr;
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
     * @return Returns the salesTeam.
     */
    public ISalesTeam getSalesTeam() {
        return salesTeam;
    }
    /**
     * @param salesTeam The salesTeam to set.
     */
    public void setSalesTeam(ISalesTeam salesTeam) {
        this.salesTeam = salesTeam;
    }
    /**
     * @return Returns the telefon.
     */
    public String getTelefon() {
        return telefon;
    }
    /**
     * @param telefon The telefon to set.
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
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
     * @return Returns the zusatz.
     */
    public String getZusatz() {
        return zusatz;
    }
    /**
     * @param zusatz The zusatz to set.
     */
    public void setZusatz(String zusatz) {
        this.zusatz = zusatz;
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

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getNachname());
        buffer.append(", ").append(getVorname())
        .append(" (#").append(getId()).append(")");
        return buffer.toString();
    }

	public boolean isAusgeschieden() {
		return ausgeschieden;
	}

	public void setAusgeschieden(boolean ausgeschieden) {
		this.ausgeschieden = ausgeschieden;
	}
	
	   /**
	 * @return the vorgesetzter
	 */
	public IMitarbeiter getVorgesetzter() {
		return vorgesetzter;
	}

	/**
	 * @param vorgesetzter the vorgesetzter to set
	 */
	public void setVorgesetzter(IMitarbeiter vorgesetzter) {
		this.vorgesetzter = vorgesetzter;
	}

	/**
	 * @return the team
	 */
	public IPicklistEntry getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(IPicklistEntry team) {
		this.team = team;
	}

	/**
	 * @return the info1
	 */
	public boolean isInfo1() {
		return info1;
	}

	/**
	 * @param info1 the info1 to set
	 */
	public void setInfo1(boolean info1) {
		this.info1 = info1;
	}
}
