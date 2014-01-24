/**
 *
 */
package de.xwic.appkit.webbase.toolkit.util.bundles;

import de.jwic.base.SessionContext;
import de.xwic.appkit.core.config.Bundle;

/**
 * @author Alexandru Bledea
 * @since Dec 12, 2013
 */
public interface IBundleProvider {

	/**
	 * @param langId
	 * @return
	 */
	public Bundle getBundle(SessionContext sessionContext);
	
	/**
	 * @param langId
	 * @return
	 */
	public Bundle getBundle(String langId);
}
