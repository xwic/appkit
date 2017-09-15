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
	 * Returns true if lazy loading should be enabled for an entity referencing property. 
	 * This is true by default.
	 * @return
	 */
	public boolean isLazy();
	
	/**
	 * Returns true if the property should be created as a Set<type> of values.
	 * @return
	 */
	public boolean isMultivalue();
	
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
	 * Returns the raw type name if it is a basic type or the interface name.
	 * @return
	 */
	public String getResolvedType();
	
	/**
	 * Returns the picklist id used by this property if it is a PicklistEntry.
	 * @return
	 */
	public String getPicklistId();
	
	/**
	 * Return list of picklist entries to be created.
	 * @return
	 */
	public List<EntityPicklistEntry> getPicklistEntries();
	
}
