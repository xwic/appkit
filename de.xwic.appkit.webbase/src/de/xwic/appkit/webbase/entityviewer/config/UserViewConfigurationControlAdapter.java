/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import de.jwic.base.Event;

/**
 * @author Adrian Ionescu
 */
public class UserViewConfigurationControlAdapter implements IUserViewConfigurationControlListener {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.entityviewer.config.IUserViewConfigurationControlListener#onAfterDelete(de.jwic.base.Event)
	 */
	@Override
	public void onConfigDeleted(Event event) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.entityviewer.config.IUserViewConfigurationControlListener#onConfigApplied(de.jwic.base.Event)
	 */
	@Override
	public void onConfigApplied(Event event) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.entityviewer.config.IUserViewConfigurationControlListener#onPublicConfigCopied(de.jwic.base.Event)
	 */
	@Override
	public void onPublicConfigCopied(Event event) {
	}
}
