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
package de.xwic.appkit.core.access;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMonitoringElementDAO;
import de.xwic.appkit.core.model.entities.IMonitoringElement;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * Caches the settings for the object monitoring for a faster decision if an object
 * needs to be marked as changed or not.
 * 
 * @author Florian Lippisch
 */
public class ObjectMonitoringSettings implements AccessListener {

	private final Log log = LogFactory.getLog(ObjectMonitoringSettings.class); 
	
	private Map<String, ObjectMonitoringTypeProperties> typeMap = new HashMap<String, ObjectMonitoringTypeProperties>();
	private ObjectMonitoringTypeProperties NOT_MONITORED_TYPE = new ObjectMonitoringTypeProperties("*");
	
	/**
	 * Constructor.
	 */
	public ObjectMonitoringSettings() {
		
	}
	
	/**
	 * Initialiy load the ObjectMonitoring settings.
	 *
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		log.info("Initialzing ObjectMonitoringSettings");
		
		int count = 0;
		IMonitoringElementDAO dao = (IMonitoringElementDAO)DAOSystem.getDAO(IMonitoringElementDAO.class);
	
		List<IMonitoringElement> entries = dao.getEntities(null, new PropertyQuery()); // read all.
		for (Iterator<IMonitoringElement> it = entries.iterator(); it.hasNext(); ) {
			IMonitoringElement me = (IMonitoringElement)it.next();
			ObjectMonitoringTypeProperties tp = typeMap.get(me.getEntityClassName());
			if (tp == null) {
				tp = new ObjectMonitoringTypeProperties(me.getEntityClassName());
				typeMap.put(me.getEntityClassName(), tp);
			}
			if (me.getPropertyName() == null) {
				tp.update(me.isMonitored());
				if (me.isMonitored()) {
					count++;
				}
			} else {
				tp.update(me.getPropertyName(), me.isMonitored());
			}
		}
		
		log.info("ObjectMonitoringSetting initialized. (" + count + " types monitored.");
		
	}
	
	/**
	 * Returns an object that is used to determine the monitoring state for object properties.
	 * If the entityType is unknown, a stub object is returned that has the state of not beeing monitored.
	 * @param entityType
	 * @return
	 */
	public ObjectMonitoringTypeProperties getMonitoringProperties(String entityType) {
		if (typeMap.containsKey(entityType)) {
			return typeMap.get(entityType);
		}
		return NOT_MONITORED_TYPE;
	}

	/* AccessListener methods */
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityDeleted(AccessEvent event) {
		
		if (event.getEntity() instanceof IMonitoringElement) {
			handleDeleted((IMonitoringElement)event.getEntity());
		}
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entitySoftDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entitySoftDeleted(AccessEvent event) {
		if (event.getEntity() instanceof IMonitoringElement) {
			handleDeleted((IMonitoringElement)event.getEntity());
		}
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityUpdated(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityUpdated(AccessEvent event) {
		if (event.getEntity() instanceof IMonitoringElement) {
			handleUpdated((IMonitoringElement)event.getEntity());
		}
	}
	
	/**
	 * @param element
	 */
	private synchronized void handleUpdated(IMonitoringElement elm) {
		ObjectMonitoringTypeProperties tp = typeMap.get(elm.getEntityClassName());
		if (tp == null) {
			tp = new ObjectMonitoringTypeProperties(elm.getEntityClassName());
			typeMap.put(elm.getEntityClassName(), tp);
		}
		if (elm.getPropertyName() == null) {
			tp.update(elm.isMonitored()); // stop beeing monitored
		} else {
			tp.update(elm.getPropertyName(), elm.isMonitored());
		}
	}

	/**
	 * @param element
	 */
	private synchronized void handleDeleted(IMonitoringElement elm) {
		if (elm.isMonitored()) {
			ObjectMonitoringTypeProperties tp = typeMap.get(elm.getEntityClassName());
			if (tp != null) {
				if (elm.getPropertyName() == null) {
					tp.update(false); // stop beeing monitored
				} else {
					tp.update(elm.getPropertyName(), false);
				}
			}
		}
	
	}

	
	
}
