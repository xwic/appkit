/**
 *
 */
package de.xwic.appkit.core.remote.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.util.EntityUtil;
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

//		AAA use inteligence to make this support all function calls

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
	 * @throws IllegalStateException if the type cannot be found
	 */
	private Object handleType(final String methodName, final Object[] args) throws IllegalStateException {
		return EntityUtil.type(eto.getEntityClass());
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
				pv.setLoaded(true);
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

			// TODO AI make this nicer!
			if (IPicklistEntry.class.isAssignableFrom(eto.getEntityClass()) && methodName.equals("getBezeichnung")) {
				IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);

				IPicklistEntry pe = dao.getPickListEntryByID(eto.getEntityId());

				IPicklistText text = dao.getPicklistText(pe, "en");

				return text != null ? text.getBezeichnung() : "";
			}

			String propertyName = getPropertyName(methodName, prefixLength);
			PropertyValue pv = eto.getPropertyValue(propertyName);
			if (pv == null) {
				throw new IllegalStateException(String.format("Unknown property '%s' for '%s': ", propertyName, eto.getEntityClass()));
			}
			if (pv.isLoaded()) {
				return pv.getValue();
			} else {
				// Lazy Load

				// figure out type
				if (IEntity.class.isAssignableFrom(pv.getType())) { // Resolve entity reference

					Class<? extends IEntity> clazz = (Class<? extends IEntity>) pv.getType();
					IEntity entity = DAOSystem.findDAOforEntity(clazz).getEntity(pv.getEntityId());

					pv.setValue(entity);
					pv.setLoaded(true);
					return entity;

				} else if (Collection.class.isAssignableFrom(pv.getType())) { // resolve collection

					IRemoteDataAccessClient remoteDataAccessClient = DAOSystem.getRemoteDataAccessClient();
					if (remoteDataAccessClient == null) {
						throw new IllegalStateException("RemoteDataAccessClient not available/configured!");
					}
					try {
						EntityDescriptor descriptor = DAOSystem.findEntityDescriptor(eto.getEntityClass().getName());
						List collection = remoteDataAccessClient.getETOCollection(descriptor.getClassname(), eto.getEntityId(), propertyName);

						Collection newCol;
						if (Set.class.isAssignableFrom(pv.getType())) {
							// need to transform into a set
							newCol = new HashSet();
						} else if (List.class.isAssignableFrom(pv.getType())) {
							newCol = new ArrayList();
						} else {
							throw new RemoteDataAccessException("Unsupported collection type: " + pv.getType().getName());
						}

						for (Object o : collection) {
							if (o instanceof EntityTransferObject) {
								newCol.add(EntityProxyFactory.createEntityProxy((EntityTransferObject) o));
							} else {
								newCol.add(o);
							}
						}

						pv.setValue(newCol);
						pv.setLoaded(true);
						return newCol;
					} catch (Exception e) {
						throw new RemoteDataAccessException("Cannot initilaize collection '" + propertyName + "' for entity " + eto.getEntityClass().getName() + ": " + e, e);
					}

				}
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
