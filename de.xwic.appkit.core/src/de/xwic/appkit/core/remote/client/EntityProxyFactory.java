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

//		i think that the classloader that contains IEntity would be a better idea
//		since etoClass is bound to have IEntity in it, the system classloader isn't
//		return (IEntity) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
		Object obj = Proxy.newProxyInstance(IEntity.class.getClassLoader(),
				interfaces,
				handler);
		try{
			return (IEntity) obj;
		}catch (ClassCastException e) {
			throw new RuntimeException("Cannot cast to IEntity. It might be that required interface is not " +
					"defined on the Entity Impl. class: " + obj.getClass());
		}
	}

}
