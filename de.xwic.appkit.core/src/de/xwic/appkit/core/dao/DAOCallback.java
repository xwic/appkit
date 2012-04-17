/*
 * de.xwic.appkit.core.dao.DAOCallback
 * Created on 11.07.2005
 *
 */
package de.xwic.appkit.core.dao;

/**
 * @author Florian Lippisch
 */
public interface DAOCallback {

	/**
	 * Execute the custom code.
	 * @param api
	 * @return
	 */
	public Object run(DAOProviderAPI api);
	
}
