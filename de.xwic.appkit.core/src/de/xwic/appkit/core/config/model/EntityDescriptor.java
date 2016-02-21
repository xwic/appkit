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
/*
 * de.pol.entitydescriptor.Entity
 * Created on 04.05.2005
 *
 */
package de.xwic.appkit.core.config.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.config.Domain;
import de.xwic.appkit.core.config.ReportTemplate;
import de.xwic.appkit.core.config.list.ListSetup;

/**
 * Describes the properties of an entity and the information on
 * how to edit, list and filter this entity.
 * @author Florian Lippisch
 */
public class EntityDescriptor {

    private String id = null;
    private String classname = null;
    private String titlePattern = null;
    private String iconKey = null;
    
    private String defaultDisplayProperty = null;
    
    private int replicationOrder = 0;
    private boolean history = false;
    private boolean transform = true;
    
    private URL emailTemplate = null;
    
    private List<ReportTemplate> reportTemplates = null;
    
    private Map<String, ListSetup> lists = null;
    private Map<String, Property> properties = null;
    private Domain domain = null;
    private boolean hidden = false;
    private boolean monitorable = false;

	/**
     * Constructor.
     */
    public EntityDescriptor() {
    	
    }
    
    /**
     * Creates a new EntityDescriptor and build the property list for the
     * specified clazz.
     * @param clazz
     * @throws IntrospectionException 
     */
    public EntityDescriptor(Class<?> clazz) throws IntrospectionException {
    	this.classname = clazz.getName();
    	this.id = classname;
    	buildPropertyList(clazz);
    }
    
    /**
	 * @param clazz
     * @throws IntrospectionException 
	 */
	private void buildPropertyList(Class<?> clazz) throws IntrospectionException {
		
		properties = new HashMap<String, Property>();
		// AI 08-Jan-2013: make it linked, the order counts (see below)
		Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		addAllInterfaces(clazz, interfaces);
		// AI 08-Jan-2013: add the current interface last, in case it overrides methods from higher interfaces
		// it can happen when we play with generics
		interfaces.add(clazz);
		for (Iterator<Class<?>> it = interfaces.iterator(); it.hasNext(); ) {
			Class<?> cl = it.next();
			BeanInfo bi = Introspector.getBeanInfo(cl);
			PropertyDescriptor[] descr = bi.getPropertyDescriptors();
			for (int i = 0; i < descr.length; i++) {
				Property prop = new Property(descr[i]);
				prop.setEntityDescriptor(this);
				properties.put(descr[i].getName(), prop);
			}
		}
		
	}

	/**
	 * @param clazz
	 */
	private void addAllInterfaces(Class<?> clazz, Set<Class<?>> interfaces) {
		Class<?>[] intfs = clazz.getInterfaces();
		if (intfs != null) { 
			for (int i = 0; i < intfs.length; i++ ) {
				interfaces.add(intfs[i]);
				addAllInterfaces(intfs[i], interfaces);
			}
		}
	}

	/**
     * @return Returns the classname.
     */
    public String getClassname() {
        return classname;
    }
    /**
     * @param classname The classname to set.
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Add a list setup.
     * @param listSetup
     */
    public void addListSetup(ListSetup listSetup) {
        if (lists == null) {
            lists = new HashMap<String, ListSetup>();
        }
        lists.put(listSetup.getListId(), listSetup);
    }
    
    /**
     * Add a ReportTemplate.
     * @param reportTemplate
     */
    public void addReportTemplate(ReportTemplate reportTemplate) {
    	if (reportTemplates == null) {
    		reportTemplates = new ArrayList<ReportTemplate>();
    	}
    	reportTemplates.add(reportTemplate);
    }
    
    /**
     * @return
     */
    public Collection<String> getListSetupIds() {
    	if (lists != null) {
    		return lists.keySet();
    	}
    	return null;
    }
    
    /**
     * Returns the list setup specified by the id.
     * @param id
     * @return
     */
    public ListSetup getListSetup(String listId) {
        if (lists != null) {
            return lists.get(listId);
        }
        return null;
    }
    
    /**
     * Returns the entity property descriptor.
     * @param name
     * @return
     */
    public Property getProperty(String name) {
    	if (properties != null) {
    		return properties.get(name);
    	}
    	return null;
    }
    
	/**
	 * @return Returns the properties in a map with the name (String) as key and the Property as value. 
	 */
	public Map<String, Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return the titlePattern
	 */
	public String getTitlePattern() {
		return titlePattern;
	}

	/**
	 * @param titlePattern the titlePattern to set
	 */
	public void setTitlePattern(String titlePattern) {
		this.titlePattern = titlePattern;
	}

	/**
	 * @return the history
	 */
	public boolean isHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(boolean history) {
		this.history = history;
	}

	/**
	 * Returns true if the object should be transfered using transport objects.
	 * @return
	 */
	public boolean isTransform() {
		return transform;
	}

	/**
	 * @param transform the transform to set
	 */
	public void setTransform(boolean transform) {
		this.transform = transform;
	}

	/**
	 * @return the replicationOrder
	 */
	public int getReplicationOrder() {
		return replicationOrder;
	}

	/**
	 * @param replicationOrder the replicationOrder to set
	 */
	public void setReplicationOrder(int replicationOrder) {
		this.replicationOrder = replicationOrder;
	}
    
    /**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	
	/**
	 * @return the emailTemplate
	 */
	public URL getEmailTemplate() {
		return emailTemplate;
	}

	/**
	 * @param emailTemplate the emailTemplate to set
	 */
	public void setEmailTemplate(URL emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	/**
	 * @return the reportTemplates
	 */
	public List<ReportTemplate> getReportTemplates() {
		return reportTemplates;
	}

	
	/**
	 * @return the iconKey
	 */
	public String getIconKey() {
		return iconKey;
	}

	
	/**
	 * @param iconKey the iconKey to set
	 */
	public void setIconKey(String iconKey) {
		this.iconKey = iconKey;
	}

	
	/**
	 * @return the monitorable
	 */
	public boolean isMonitorable() {
		return monitorable;
	}

	/**
	 * @param monitorable the monitorable to set
	 */
	public void setMonitorable(boolean monitorable) {
		this.monitorable = monitorable;
	}

	/**
	 * @return the defaultDisplayProperty
	 */
	public String getDefaultDisplayProperty() {
		return defaultDisplayProperty;
	}

	/**
	 * @param defaultDisplayProperty the defaultDisplayProperty to set
	 */
	public void setDefaultDisplayProperty(String defaultDisplayProperty) {
		this.defaultDisplayProperty = defaultDisplayProperty;
	}
}
