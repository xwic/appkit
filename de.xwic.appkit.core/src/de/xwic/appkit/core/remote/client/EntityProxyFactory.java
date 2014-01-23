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
	public static IEntity createEntityProxy(final EntityTransferObject eto) {

		final ETOProxyHandler handler = new ETOProxyHandler(eto);

		// build list of interfaces
		final Class[] interfaces = eto.getEntityClass().getInterfaces();

		return (IEntity) Proxy.newProxyInstance(getClassLoader(interfaces),
				interfaces,
				handler);
	}

	/**
	 * @param interfaces
	 * @return
	 */
	private static ClassLoader getClassLoader(final Class... interfaces) {
		if (interfaces != null && interfaces.length > 0) {
			return interfaces[0].getClassLoader();
		}
//		i think that the classloader that contains IEntity would be a better idea
//		since etoClass is bound to have IEntity in it, the system classloader isn't
//		return ClassLoader.getSystemClassLoader();
		return IEntity.class.getClassLoader();
	}

}
