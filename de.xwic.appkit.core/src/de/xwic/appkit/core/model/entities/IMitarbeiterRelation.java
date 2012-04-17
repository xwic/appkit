package de.xwic.appkit.core.model.entities;

import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;


/**
 * The mitarbeiter relation entity.
 * @author Aron Cotrau
 */
public interface IMitarbeiterRelation extends IEntity {

	/** picklist key for the rolle property */
	public static final String MIT_RELATION_ROLLE_PL_KEY = "mitRelation.rolle";
	
	/**
	 * @return the mitarbeiter
	 */
	public IMitarbeiter getMitarbeiter();

	/**
	 * @param mitarbeiter the mitarbeiter to set
	 */
	public void setMitarbeiter(IMitarbeiter mitarbeiter);

	/**
	 * @return the relatedMitarbeiter
	 */
	public IMitarbeiter getRelatedMitarbeiter();

	/**
	 * @param relatedMitarbeiter the relatedMitarbeiter to set
	 */
	public void setRelatedMitarbeiter(IMitarbeiter relatedMitarbeiter);

	/**
	 * @return the rolle
	 */
	public IPicklistEntry getRolle();

	/**
	 * @param rolle the rolle to set
	 */
	public void setRolle(IPicklistEntry rolle);

	/**
	 * @return the startDate
	 */
	public Date getStartDate();

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate);

	/**
	 * @return the endDate
	 */
	public Date getEndDate();

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate);

}