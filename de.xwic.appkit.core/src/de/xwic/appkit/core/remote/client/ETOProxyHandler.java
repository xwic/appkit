/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.IEntityInvocationHandler;
import de.xwic.appkit.core.transfer.PropertyValue;
import de.xwic.appkit.core.util.Equals;

/**
 * @author lippisch
 *
 */
public class ETOProxyHandler implements InvocationHandler, IEntityInvocationHandler {

	private EntityTransferObject eto;
	
	/**
	 * @param eto
	 */
	public ETOProxyHandler(EntityTransferObject eto) {
		super();
		this.eto = eto;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.transfer.IEntityInvocationHandler#getEntityImplClass()
	 */
	@Override
	public Class<?> getEntityImplClass() {
		return eto.getEntityClass();
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		String methodName = method.getName();
		
		if (methodName.startsWith("get")) {
			// is a getter -> find out the property name
			return handleGetter(methodName, 3);
		} else if (methodName.startsWith("is")) {
			return handleGetter(methodName, 2);
			
		} else if (methodName.startsWith("set")) {
			return handleSetter(methodName, args);
			
		} else if (methodName.equals("type")) {
			return handleType(methodName, args);
			
		} else if (methodName.equals("equals")) {
			return handleEquals(methodName, args);
			
		} else if (methodName.equals("hashCode")) {
			return handleHashCode(methodName, args);
		}
		
		return null;
	}

	/**
	 * @param methodName
	 * @param args
	 * @return
	 */
	private Object handleHashCode(String methodName, Object[] args) {
		return eto.hashCode();
	}

	/**
	 * @param methodName
	 * @param args
	 * @return
	 */
	private Object handleEquals(String methodName, Object[] args) {
		if (args != null && args.length == 1) {
			return eto.equals(args[0]);
		}
		
		return false;
	}

	/**
	 * @param methodName
	 * @param args
	 * @return
	 */
	private Object handleType(String methodName, Object[] args) {
		// we get the implementation class, but we need the interface
		return DAOSystem.findDAOforEntity(eto.getEntityClass().getName()).getEntityClass();
	}

	/**
	 * @param methodName
	 * @param args
	 * @return
	 */
	private Object handleSetter(String methodName, Object[] args) {
		
		String propertyName = getPropertyName(methodName, 3);
		if (args != null && args.length == 1) {

			Object newValue = args[0];
			PropertyValue pv = eto.getPropertyValue(propertyName);
			if (pv == null) {
				throw new IllegalStateException("No such property: " + propertyName);
			}
			if (pv.getAccess() != ISecurityManager.READ_WRITE) {
				throw new DataAccessException("No RW access to this property (" + propertyName + ")");
			}
			
			if (!Equals.equal(pv.getValue(), newValue)) {
				pv.setValue(args[0]);
				pv.setModified(true);
				eto.setModified(true);
			}
		}
		
		return null;
	}

	/**
	 * @param methodName
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object handleGetter(String methodName, int prefixLength) {

		if (methodName.length() > prefixLength) {

			String propertyName = getPropertyName(methodName, prefixLength);
			PropertyValue pv = eto.getPropertyValue(propertyName);
			if (pv == null) {
				throw new IllegalStateException("Unknown property: " + propertyName);
			}
			if (pv.isLoaded()) {
				return pv.getValue();
			} else {
				// Lazy Load 
				Class<? extends IEntity> clazz = (Class<? extends IEntity>) pv.getType();
				IEntity entity = DAOSystem.findDAOforEntity(clazz).getEntity(pv.getEntityId());
				
				pv.setValue(entity);
				pv.setLoaded(true);
				return entity;
			}
			
		}
		return null;
	}

	/**
	 * @param methodName
	 * @param prefixLength
	 * @return
	 */
	private String getPropertyName(String methodName, int prefixLength) {

		String suffix = methodName.substring(prefixLength);
		if (suffix.length() > 1) { // more than 2 characters
			if (suffix.charAt(1) >= 'A' && suffix.charAt(1) <= 'Z') {
				// ALL uppercase as it seems, take as is
				return suffix;
			} else {
				return suffix.substring(0, 1).toLowerCase() + suffix.substring(1);
			}
		} else { // just one character, i.e. getA() -> property name is 'a'
			return suffix.toLowerCase();
		}
		
	}

}
