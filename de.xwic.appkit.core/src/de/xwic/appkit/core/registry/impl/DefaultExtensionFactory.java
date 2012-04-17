/**
 * 
 */
package de.xwic.appkit.core.registry.impl;

import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.core.registry.IExtensionFactory;

/**
 * @author Adrian Ionescu
 */
public class DefaultExtensionFactory implements IExtensionFactory {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtensionFactory#createExtension(de.xwic.appkit.core.registry.IExtension)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object createExtension(IExtension extension) throws Exception {
		Class cl = Class.forName(extension.getClassName());
		return cl.newInstance();
	}

}
