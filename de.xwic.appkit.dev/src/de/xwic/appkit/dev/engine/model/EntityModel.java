/**
 * 
 */
package de.xwic.appkit.dev.engine.model;

import java.util.List;

import de.xwic.appkit.dev.engine.ConfigurationException;

/**
 * @author lippisch
 *
 */
public interface EntityModel {

	/**
	 * Returns the name of the entity.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the list of properties defined.
	 * @return
	 * @throws ConfigurationException 
	 */
	public List<EntityProperty> getProperties() throws ConfigurationException;
	
	/**
	 * Returns the name of the ID column.
	 * @return
	 */
	public String getIdColumn();
	
	/**
	 * Returns the name of the ID column on history table.
	 * @return
	 */
	public String getHisIdColumn();
	
	/**
	 * Returns the tablename of the entity.
	 * @return
	 */
	public String getTableName();
	
	/**
	 * Returns the property used for sorting the entity by default or displaying a humand readable expression of it. 
	 * @return
	 */
	public String getDefaultDisplayProperty();
	
	/**
	 * Return the description attribute of the entity.
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Returns true if history dao should be generated.
	 * @return
	 */
	public boolean isHistoryEntity();
	
}
