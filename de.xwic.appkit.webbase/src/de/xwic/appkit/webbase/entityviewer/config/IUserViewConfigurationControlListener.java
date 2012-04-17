/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import de.jwic.base.Event;

/**
 * @author Adrian Ionescu
 */
public interface IUserViewConfigurationControlListener {

	/**
	 * @param event
	 */
	public void onConfigDeleted(Event event);
	
	/**
	 * @param event 
	 */
	public void onConfigApplied(Event event);
	
	/**
	 * @param event
	 */
	public void onPublicConfigCopied(Event event);
}
