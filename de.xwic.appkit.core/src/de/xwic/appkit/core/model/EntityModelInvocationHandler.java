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
package de.xwic.appkit.core.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Handles EntityModel invocation.
 * @author Florian Lippisch
 */
public class EntityModelInvocationHandler implements InvocationHandler {

	private EntityModelImpl model;
	
	/**
	 * @param entity
	 */
	public EntityModelInvocationHandler(IEntity entity) {
		model = new EntityModelImpl(entity);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		if (method.getDeclaringClass().isAssignableFrom(IEntityModel.class)) {
			// internal control method
			return method.invoke(model, args);
		} else {
			
			// handle entity methods.
			String methodName = method.getName();
			if (methodName.startsWith("get") && args == null) {
				return model.getProperty(propertyName(methodName.substring(3)));
			} else if (methodName.startsWith("is") && args == null) {
				return model.getProperty(propertyName(methodName.substring(2)));
			} else if (methodName.startsWith("set") && args.length == 1) {
				model.setProperty(propertyName(methodName.substring(3)), args[0]);
				return null;
			} else if (methodName.equals("type")) {
				return model.getOriginalEntity().type();
			} else if (methodName.equals("toString")) {
				return model.toString();
			} else if (methodName.equals("equals")) {
				// TODO implement
			} else if (methodName.equals("hashCode")) {
				// TODO implement
			}
			
		}
		
		return null;
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String propertyName(String key) {
		if (key.length() > 1 && key.charAt(1) < 'a') {
			return key;
		}
		// convert first char to lower case
		return key.substring(0, 1).toLowerCase() + key.substring(1);
	}

	/**
	 * @return
	 */
	public IEntityModel getEntityModelImpl() {
		return model;
	}


}
