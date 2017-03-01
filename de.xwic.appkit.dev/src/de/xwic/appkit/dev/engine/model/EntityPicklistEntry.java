/**
 * 
 */
package de.xwic.appkit.dev.engine.model;

/**
 * @author lippisch
 *
 */
public interface EntityPicklistEntry {

	/**
	 * The key for the entry.
	 * @return
	 */
	public String getKey();
	
	/**
	 * The default title. If no title is given, the key is used.
	 * @return
	 */
	public String getDefaultTitle();
	
	/**
	 * Optional order id (int).
	 * @return
	 */
	public String getOrder();
	
}
