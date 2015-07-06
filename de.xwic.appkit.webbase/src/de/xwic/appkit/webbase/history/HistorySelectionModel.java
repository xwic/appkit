package de.xwic.appkit.webbase.history;

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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Data Model for the History-Selection Wizard.
 * <p>
 * 
 * @author Ronny Pfretzschner
 */
public class HistorySelectionModel {

	private List<IEntity> allHistoryObjects = null;
	private Collection<?> selectedHisKeys = null;

	private Map<Object, Object> previousProps = new HashMap<Object, Object>();
	private List<PropertyVersionHelper> changes = new ArrayList<PropertyVersionHelper>();

	private final IEntity baseEntity;
	private Bundle bundle = null;

	/**
	 * @param entity
	 */
	public HistorySelectionModel(IEntity entity) {
		this.baseEntity = entity;
		try {
			bundle = ConfigurationManager.getSetup().getDomain("core").getBundle(Locale.getDefault().getLanguage());
		} catch (ConfigurationException e) {
			bundle = null;
		}
	}

	/**
	 * Returns all relevant HistoryObjects of a given entity in a List.
	 * <p>
	 * An empty List is returned, if entity is null. <br>
	 * CAUTION!!! Works actually just with UNTERNEHMEN AND MANDAT!!!! RPF
	 * 20-09-2005
	 * 
	 * @param entity
	 * @return A List with HistoryObjects
	 */
	public List<IEntity> getEntityRelatedHistoryObjects(IEntity entity) {
		if (allHistoryObjects != null && !allHistoryObjects.isEmpty()) {
			allHistoryObjects.clear();
		}
		if (entity == null) {
			return new ArrayList<IEntity>();
		}

		DAO dao = DAOSystem.findDAOforEntity(entity.type());
		allHistoryObjects = dao.getHistory(entity);

		return allHistoryObjects;
	}

	/**
	 * Generates a List of PropertyHelper objects, which can be displayed in a
	 * tableviewer.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PropertyVersionHelper> generatePropertyVersionsList() throws Exception {
		List<IEntity> entities = getSelectedHistoryEntities();
		return generatePropertyVersionsList(entities);
	}

	/**
	 * @return
	 */
	public List<IEntity> getSelectedHistoryEntities() {
		List<IEntity> entities = new ArrayList<IEntity>();
		DAO dao = DAOSystem.findDAOforEntity(baseEntity.type());
		List<IEntity> history = dao.getHistory(baseEntity);
		
		for (IEntity entity : history) {
			for (Object obj : getSelectedHisKeys()) {
				int id = Integer.parseInt(obj.toString());
				if (entity.getId() == id) {
					entities.add(entity);
					break;
				}
			}
		}

		return entities;
	}

	/**
	 * Generates a List of PropertyHelper objects, which can be displayed in a
	 * tableviewer.
	 * <p>
	 * 
	 * @param allSelHisObjs
	 *            Selection of HistoryObjects
	 * @return List
	 * @throws Exception
	 */
	public List<PropertyVersionHelper> generatePropertyVersionsList(List<IEntity> allSelHisObjs) throws Exception {
		// clear old lists, maps first...
		previousProps.clear();
		changes.clear();

		if (null == allSelHisObjs || allSelHisObjs.isEmpty()) {
			return changes;
		}

		IEntity entityD = (IEntity) allSelHisObjs.get(0);
		DAO dao = DAOSystem.findDAOforEntity(entityD.type().getName());
		EntityDescriptor descrip = ConfigurationManager.getSetup().getEntityDescriptor(entityD.type().getName());

		// get reflection stuff
		BeanInfo info = Introspector.getBeanInfo(entityD.getClass());
		PropertyDescriptor[] descs = info.getPropertyDescriptors();

		// go through all properties
		for (int i = 0; i < descs.length; i++) {
			Method readPropMeth = descs[i].getReadMethod();
			String propName = descs[i].getName();

			Property prop = descrip.getProperty(propName);

			// check right before getting on... if no right to
			// access the property, ignore for history at all!
			if (prop != null && !prop.hasReadAccess()) {
				continue;
			}

			PropertyVersionHelper helperPropertyStorage = new PropertyVersionHelper(readPropMeth, propName);

			// check properties, which have to be ignored
			boolean ignore = propName.equals("entityVersion") || propName.equals("lastModifiedAt")
					|| propName.equals("createdAt") || propName.equals("historyReason") || propName.equals("id")
					|| propName.equals("localChanged");

			if (ignore) {
				continue;
			}

			// go through all versions objects
			for (int j = 0; j < allSelHisObjs.size(); j++) {
				IEntity entity = (IEntity) allSelHisObjs.get(j);
				HistoryVersion versionWrap = new HistoryVersion(entity, false);
				helperPropertyStorage.addVersion(j + 1, versionWrap);

				// get actual value
				Object actualValue = readPropMeth.invoke(entity, (Object[]) null);

				// just a test to get the right version of the object ;)
				if ((actualValue instanceof IEntity) && (!(actualValue instanceof IPicklistEntry))
						&& prop.isEmbeddedEntity()) {
					actualValue = dao.findCorrectVersionForEntityRelation(entity, (IEntity) actualValue);
				}

				// load all values of 1. version
				if (j == 0) {
					HistoryVersion versionWrap2 = new HistoryVersion(entity, false);
					helperPropertyStorage.addVersion(0, versionWrap2);
					previousProps.put(readPropMeth, actualValue);
					continue;
				}

				// get previous value...
				Object prevPropValue = previousProps.get(readPropMeth);

				// replace previous value with new one
				previousProps.put(readPropMeth, actualValue);

				boolean changed = false;

				// if both values not null -> compare with equals
				if (actualValue != null && prevPropValue != null) {

					if (!actualValue.equals(prevPropValue)) {
						changed = true;
					}
				}

				// if one value null the other not -> changed!
				else if (actualValue == null && prevPropValue != null) {
					changed = true;
				}
				// if one value null the other not -> changed!
				else if (prevPropValue == null && actualValue != null) {
					changed = true;
				}

				if (changed) {
					versionWrap.setChanged(true);
					// remove first necessary, list does not overwrite with
					// add...
					changes.remove(helperPropertyStorage);
					// add again
					changes.add(helperPropertyStorage);
				}
			}
		}
		return changes;
	}

	/**
	 * @return a List reference of all selected HistoryObjects
	 */
	public List<IEntity> getAllHistoryObjects() {
		return allHistoryObjects;
	}

	/**
	 * @return the baseEntity
	 */
	public IEntity getBaseEntity() {
		return baseEntity;
	}

	/**
	 * @return the selectedHisEntities
	 */
	public Collection<?> getSelectedHisKeys() {
		return selectedHisKeys;
	}

	/**
	 * @param selectedHisKeys
	 *            the selectedHisEntities to set
	 */
	public void setSelectedHisKeys(Collection<?> selectedHisKeys) {
		this.selectedHisKeys = selectedHisKeys;
	}

	/**
	 * @param key
	 * @return
	 */
	public String getResourceString(String key) {
		if (null != bundle) {
			return bundle.getString(key);
		}

		return "!" + key + "!";
	}

}
