/**
 * 
 */
package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

import java.math.BigDecimal;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization.
 * 
 * @author lippisch
 */
public interface ICompany extends IEntity {

	public static final String PL_COMPANY_TYPE = "company.type";
	public static final String PL_COMPANY_RELATIONSHIP = "company.relationship";

	/**
	 * @return the type
	 */
	public abstract IPicklistEntry getType();

	/**
	 * @param type the type to set
	 */
	public abstract void setType(IPicklistEntry type);

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @param name the name to set
	 */
	public abstract void setName(String name);

	/**
	 * @return the relationship
	 */
	public abstract Set<IPicklistEntry> getRelationship();

	/**
	 * @param relationship the relationship to set
	 */
	public abstract void setRelationship(Set<IPicklistEntry> relationship);

	/**
	 * @return the phone1
	 */
	public abstract String getPhone1();

	/**
	 * @param phone1 the phone1 to set
	 */
	public abstract void setPhone1(String phone1);

	/**
	 * @return the fax
	 */
	public abstract String getFax();

	/**
	 * @param fax the fax to set
	 */
	public abstract void setFax(String fax);

	/**
	 * @return the webSite
	 */
	public abstract String getWebSite();

	/**
	 * @param webSite the webSite to set
	 */
	public abstract void setWebSite(String webSite);

	/**
	 * @return the email1
	 */
	public abstract String getEmail1();

	/**
	 * @param email1 the email1 to set
	 */
	public abstract void setEmail1(String email1);

	/**
	 * @return the email2
	 */
	public abstract String getEmail2();

	/**
	 * @param email2 the email2 to set
	 */
	public abstract void setEmail2(String email2);

	/**
	 * @return the notes
	 */
	public abstract String getNotes();

	/**
	 * @param notes the notes to set
	 */
	public abstract void setNotes(String notes);

	/**
	 * @return the segment
	 */
	public abstract IPicklistEntry getSegment();

	/**
	 * @param segment the segment to set
	 */
	public abstract void setSegment(IPicklistEntry segment);

	BigDecimal getAnnualRevenue();

	void setAnnualRevenue(BigDecimal annualRevenue);

	Integer getNumberOfEmployees();

	void setNumberOfEmployees(Integer numberOfEmployees);


	/**
	 * @return the city
	 */
	public abstract String getCity();

	/**
	 * @param city the city to set
	 */
	public abstract void setCity(String city);

	/**
	 * @return the zip
	 */
	public abstract String getZip();

	/**
	 * @param zip the zip to set
	 */
	public abstract void setZip(String zip);

	/**
	 * @return the address1
	 */
	public abstract String getAddress1();

	/**
	 * @param address1 the address1 to set
	 */
	public abstract void setAddress1(String address1);

	/**
	 * @return the address2
	 */
	public abstract String getAddress2();

	/**
	 * @param address2 the address2 to set
	 */
	public abstract void setAddress2(String address2);

	/**
	 * @return the state
	 */
	public abstract String getState();

	/**
	 * @param state the state to set
	 */
	public abstract void setState(String state);

	/**
	 * @return the country
	 */
	public abstract IPicklistEntry getCountry();

	/**
	 * @param country the country to set
	 */
	public abstract void setCountry(IPicklistEntry country);

}
