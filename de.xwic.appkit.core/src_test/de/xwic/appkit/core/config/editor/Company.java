/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *******************************************************************************/
package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization. 
 * 
 */
public class Company extends Entity implements ICompany  {

	private IPicklistEntry type;
	private String name = null;

	private Set<IPicklistEntry> relationship;
	
	private String city = null;
	private String zip = null;
	private String address1 = null;
	private String address2 = null;
	private String state = null;
	private IPicklistEntry country = null;

	private String phone1 = null;
	private String fax = null;
	private String webSite = null;
	private String email1 = null;
	private String email2 = null;

	private String notes = null;

	private IPicklistEntry segment = null;

	private BigDecimal annualRevenue;

	private Integer numberOfEmployees;

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getType()
	 */
	@Override
	public IPicklistEntry getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setType(de.xwic.appkit.core.model.entities.IPicklistEntry)
	 */
	@Override
	public void setType(IPicklistEntry type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getRelationship()
	 */
	@Override
	public Set<IPicklistEntry> getRelationship() {
		return relationship;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setRelationship(java.util.Set)
	 */
	@Override
	public void setRelationship(Set<IPicklistEntry> relationship) {
		this.relationship = relationship;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getPhone1()
	 */
	@Override
	public String getPhone1() {
		return phone1;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setPhone1(java.lang.String)
	 */
	@Override
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getFax()
	 */
	@Override
	public String getFax() {
		return fax;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setFax(java.lang.String)
	 */
	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getWebSite()
	 */
	@Override
	public String getWebSite() {
		return webSite;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setWebSite(java.lang.String)
	 */
	@Override
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getEmail1()
	 */
	@Override
	public String getEmail1() {
		return email1;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setEmail1(java.lang.String)
	 */
	@Override
	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getEmail2()
	 */
	@Override
	public String getEmail2() {
		return email2;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setEmail2(java.lang.String)
	 */
	@Override
	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getNotes()
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setNotes(java.lang.String)
	 */
	@Override
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#getSegment()
	 */
	@Override
	public IPicklistEntry getSegment() {
		return segment;
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.ICompany#setSegment(de.xwic.appkit.core.model.entities.IPicklistEntry)
	 */
	@Override
	public void setSegment(IPicklistEntry segment) {
		this.segment = segment;
	}

	@Override
	public BigDecimal getAnnualRevenue() {
		return annualRevenue;
	}

	@Override
	public void setAnnualRevenue(BigDecimal annualRevenue) {
		this.annualRevenue = annualRevenue;
	}

	@Override
	public Integer getNumberOfEmployees() {
		return numberOfEmployees;
	}

	@Override
	public void setNumberOfEmployees(Integer numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String getAddress1() {
		return address1;
	}

	@Override
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Override
	public String getAddress2() {
		return address2;
	}

	@Override
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public IPicklistEntry getCountry() {
		return country;
	}

	@Override
	public void setCountry(IPicklistEntry country) {
		this.country = country;
	}
}
