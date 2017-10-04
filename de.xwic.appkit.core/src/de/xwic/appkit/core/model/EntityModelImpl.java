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

import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Implementation of the EntityModel. This class is used by the EntityModelInvocationHandler
 * to manage the IEntityModel methods.
 * 
 * @author Florian Lippisch
 */
class EntityModelImpl implements IEntityModel {

	private IEntity entity = null;
	private PropertyChangeSupport listeners = null;
	private boolean modified = false;
	
	/** stores the modified properties */
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * Constructor.
	 */
	public EntityModelImpl(IEntity entity) {
		this.entity = entity;
		this.listeners = new PropertyChangeSupport(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.model.IEntityModel#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener pListener) {
		
		PropertyChangeListener[] list = listeners.getPropertyChangeListeners();
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(pListener)){
				return;
			}	
		} 
		listeners.addPropertyChangeListener(pListener);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.IEntityModel#commit()
	 */
	@Override
	public void commit() throws EntityModelException {
		
		try {
			for (Iterator<String> it = data.keySet().iterator(); it.hasNext(); ) {
				String key = it.next();
				Object value = data.get(key);
				PropertyDescriptor pd = new PropertyDescriptor(key, entity.getClass());
				Method mWrite = pd.getWriteMethod();
				IEntityModel model = null;
				if (value != null && value instanceof IEntityModel) {
					model = (IEntityModel)value;
					value = model.getOriginalEntity();
				}
				try {
					mWrite.invoke(entity, new Object[] { value });
				} catch (InvocationTargetException ie) {
					if (ie.getTargetException() instanceof SecurityException) {
						// ignore
					} else {
						throw ie;
					}
				}
				if (model != null) {
					model.commit();
				}
			}
		} catch (Exception e) {
			throw new EntityModelException("Error commiting model: " + e, e);
		}
		data.clear();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.IEntityModel#getOriginalEntity()
	 */
	@Override
	public IEntity getOriginalEntity() {
		return entity;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.IEntityModel#isModified()
	 */
	@Override
	public boolean isModified() {
		return modified;
	}
	
	/**
	 * Fire the propertyChangeEvent.
	 * @param pPropName
	 * @param pOldValue
	 * @param pNewValue
	 */
	public void firePropertyChange(String pPropName, Object pOldValue, Object pNewValue) {
		boolean c = false;
		if (pOldValue == null) {
			c = pNewValue != null;
		} else {
			c = !pOldValue.equals(pNewValue);
		}
		if (c) {
			modified = true;
			listeners.firePropertyChange(pPropName, pOldValue, pNewValue);
		}
	}

	/**
	 * Remove a PropertyChangeListener.
	 * @param pListener
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener pListener) {
		listeners.removePropertyChangeListener(pListener);
	}

	/**
	 * Returns the value of the specified property. If the property has been
	 * modified, it is returned from the internal temporary table.
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getProperty(String name) throws Exception {
		
		if (data.containsKey(name)) {
			return data.get(name);
		}
		
		char chars[] = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		String readMethodName = "is" + new String(chars);
		
		PropertyDescriptor pd = new PropertyDescriptor(name, entity.getClass(), readMethodName, null);
		Method mRead = pd.getReadMethod();
		Object value = mRead.invoke(entity, (Object[]) null);
		if (value instanceof Collection) {
			// collections must be unmodifieable, otherwise we dont recognize changes
			// to the collection. As improvement we could create a collection proxy
			// that would check changes, but that is not required at the moment.
			if (value instanceof Set) {
				return Collections.unmodifiableSet((Set<?>)value);
			}
			return Collections.unmodifiableCollection((Collection<?>)value);
		} else if (value instanceof IPicklistEntry) {
			// use value as it is, dont transform into a model!
		} else if (value instanceof IEntity) {
			// a proxy is required 
			IEntityModel refModel = EntityModelFactory.createModel((IEntity)value);
			data.put(name, refModel);
			value = refModel;
		}
		return value;
		
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public void setProperty(String name, Object value) throws Exception {

		Object oldValue = getProperty(name);
		data.put(name, value);
		firePropertyChange(name, oldValue, value);
		
	}
	
	/**
	 * @return
	 * @throws Exception 
	 */
	public List<ChangeLogHelper> generateChangeLogHelpers() throws Exception {
		List<ChangeLogHelper> result = new ArrayList<>();
		for (Entry<String, Object> dataElem : data.entrySet()) {
			
			String name = dataElem.getKey();
			char chars[] = name.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			String readMethodName = "is" + new String(chars);
			
			PropertyDescriptor pd = new PropertyDescriptor(name, entity.getClass(), readMethodName, null);
			Method mRead = pd.getReadMethod();
			Object origValue = mRead.invoke(entity, (Object[]) null);
			Object newValue = dataElem.getValue();
			ChangeLogHelper helper = new ChangeLogHelper();
			helper.setOrigValue(origValue);
			helper.setNewValue(newValue);
			helper.setFieldNameKey(entity.type().getName() + "." + name);
			result.add(helper);
		}
		return result;
	}
	

}
