/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 *
 */
package de.xwic.appkit.core.remote.client;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashSet;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.transfer.EntityTransferObject;

/**
 * @author lippisch
 *
 */
public final class EntityProxyFactory {

    /**
     *
     */
    private EntityProxyFactory() {
    }

	/**
	 * Create a proxy object for the ETO.
	 * @param eto
	 * @return
	 */
	public static IEntity createEntityProxy(final EntityTransferObject eto) {

		final ETOProxyHandler handler = new ETOProxyHandler(eto);

		// build list of interfaces
		final Class<?>[] interfaces = getAllInterfaces(eto.getEntityClass());

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

	/**
     * @param from
     * @return
     */
    private static Class<?>[] getAllInterfaces(final Class<?> from) {
        final Collection<Class<?>> interfaces = new HashSet<Class<?>>(); // we need them unique for proxies
        if (null != from && from.isInterface()) {
            interfaces.add(from);
        }
        Class<?> inter = from;
        while (null != inter) {
            for (final Class<?> class1 : inter.getInterfaces()) {
                interfaces.add(class1);
            }
            inter = inter.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

}
