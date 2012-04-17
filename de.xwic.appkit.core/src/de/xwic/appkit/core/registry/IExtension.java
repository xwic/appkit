/**
 * 
 */
package de.xwic.appkit.core.registry;

import java.util.Map;

/**
 * @author Adrian Ionescu
 */
public interface IExtension {

	/**
	 * @return
	 */
	public String getExtensionPointId();
	
	/**
	 * @return
	 */
	public String getId();
	
	/**
	 * @return
	 */
	public int getPriority();
	
	/**
	 * @return
	 */
	public String getClassName();
	
	/**
	 * @return
	 */
	public IExtensionFactory getExtensionFactory();
	
	/**
	 * @return
	 * @throws Exception 
	 */
	public Object createExtensionObject() throws Exception;
	
	/**
	 * @param key
	 * @param value
	 */
	public void addAttribute(String key, String value);
	
	/**
	 * @param key
	 * @return
	 */
	public String getAttribute(String key);
	
	/**
	 * @return
	 */
	public Map<String, String> getAllAttributes();
	
	/**
	 * @return
	 */
	public int getSortIndex();
}
