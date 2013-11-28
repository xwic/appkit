/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.lang.reflect.Proxy;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.transfer.EntityTransferObject;

/**
 * @author lippisch
 *
 */
public class EntityProxyFactory {

	/**
	 * Create a proxy object for the ETO.
	 * @param eto
	 * @return
	 */
	public static IEntity createEntityProxy(EntityTransferObject eto) {
		
		ETOProxyHandler handler = new ETOProxyHandler(eto);

		// build list of interfaces
		Class[] interfaces = eto.getEntityClass().getInterfaces();
		
		return (IEntity) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), 
				interfaces, 
				handler);
	}
	
}
