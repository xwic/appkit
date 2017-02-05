/**
 * 
 */
package de.xwic.appkit.dev.engine.model;

import java.util.List;

/**
 * @author lippisch
 *
 */
public interface EntityProperty {

	/**
	 * Entity Name.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the name of the 'getter' method for this property. 
	 * @return
	 */
	public String getNameGetter();
	
	/**
	 * Returns the setter name.
	 * @return
	 */
	public String getNameSetter();
	
	/**
	 * @return the type
	 */
	public String getType();

	/**
	 * @return the length
	 */
	public String getLength();

	/**
	 * @return the required
	 */
	public boolean isRequired();
	
	/**
	 * Returns the column name.
	 * @return
	 */
	public String getColumnName();
	
	/**
	 * Returns true if the type of the property is a basic type such as String or int.
	 * @return
	 */
	public boolean isBasicType();
	
	/**
	 * Returns the picklist id used by this property if it is a PicklistEntry.
	 * @return
	 */
	public String getPicklistId();
	
	
	public List<EntityPicklistEntry> getPicklistEntries();
	
}
