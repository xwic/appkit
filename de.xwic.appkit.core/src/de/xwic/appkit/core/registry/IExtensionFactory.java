/**
 * 
 */
package de.xwic.appkit.core.registry;

/**
 * @author Adrian Ionescu
 */
public interface IExtensionFactory {

	/**
	 * @param extension
	 * @return
	 */
	public Object createExtension(IExtension extension) throws Exception;
	
}
