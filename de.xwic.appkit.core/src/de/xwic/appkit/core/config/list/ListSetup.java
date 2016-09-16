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
 * de.pol.entitydescriptor.ListSetup
 * Created on 31.05.2005
 *
 */
package de.xwic.appkit.core.config.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.config.editor.EComposite;
import de.xwic.appkit.core.config.model.EntityDescriptor;

/**
 * Defines the setup of a list (table).
 * @author Florian Lippisch
 */
public class ListSetup {
	
	/** create-new-entry action */
	public final static String ACTION_CREATE = "CREATE";
	/** add existing entity action */
	public final static String ACTION_ADD = "ADD";
	/** delete or remove selected entity */
	public final static String ACTION_DELETE = "DELETE";
	/** edit selected entity */
	public final static String ACTION_EDIT = "EDIT";
	/** Open an entity in read-only mode */
	public final static String ACTION_OPEN = "OPEN";
	
	//public static String ID_USER = "dummy";
	private String listId;
	private String folderId;
    private String typeClass;
    private String baseTypeAttribute = null;
    private String iconKey = null;
    private String defaultSearchProperties = null;
    private String extendsID = null;
    
    private EntityDescriptor entityDescriptor = null;
    private boolean queryEntities = true; // default
    private List<ListColumn> columns = new ArrayList<ListColumn>();
    
    private EComposite quickSearchComposite = null;
    
    private Set<String> actions = new HashSet<String>();
    
    private boolean merged = false;
    
    /**
     * Add an available action.
     * @param action
     */
    public void addAction(String action) {
    	actions.add(action);
    }
    
    /**
     * Returns true if the specified action is enabled.
     * @param action
     * @return
     */
    public boolean isActionEnabled(String action) {
    	return actions.contains(action);
    }
    
    /**
     * Returns the list of enabled actions.
     * @return
     */
    public Set<String> getActions() {
    	return actions;
    }
    
    /**
     * Add a column to the list.
     * @param column
     */
    public void addColumn(ListColumn column) {
        columns.add(column);
    }

    /**
     * Returns the list of columns.
     * @return
     */
    public List<ListColumn> getColumns() {
        return columns;
    }
    
    /**
     * @return Returns the listId.
     */
    public String getListId() {
		return listId;
	}
    
    /**
     * @param listId The listId to set.
     */
    public void setListId(String listId) {
		this.listId = listId;
	}

    /**
     * 
     * @return
     */
	public boolean isQueryEntities() {
		return queryEntities;
	}

	/**
	 * 
	 * @param queryEntities
	 */
	public void setQueryEntities(boolean queryEntities) {
		this.queryEntities = queryEntities;
	}

	/**
	 * @return the typeClass
	 */
	public String getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass the typeClass to set
	 */
	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	/**
	 * @return the folderId
	 */
	public String getFolderId() {
		return folderId;
	}

	/**
	 * @param folderId the folderId to set
	 */
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	/**
	 * @return the entityDescriptor
	 */
	public EntityDescriptor getEntityDescriptor() {
		return entityDescriptor;
	}

	/**
	 * @param entityDescriptor the entityDescriptor to set
	 */
	public void setEntityDescriptor(EntityDescriptor entityDescriptor) {
		this.entityDescriptor = entityDescriptor;
	}

	/**
	 * @return the quickSearchComposite
	 */
	public EComposite getQuickSearchComposite() {
		return quickSearchComposite;
	}

	/**
	 * @param quickSearchComposite the quickSearchComposite to set
	 */
	public void setQuickSearchComposite(EComposite quickSearchComposite) {
		this.quickSearchComposite = quickSearchComposite;
	}

	/**
	 * This method returns the name of the attribute to a related base entity. <p>
	 * 
	 * For example: Unternehmen is opened in editor, below, the Kontakt is
	 * displayed. <br>
	 * In that case, this method returns "firma", which is the related attribute<br>
	 * name to the unternehmen.<br><br>
	 * 
	 * The configuration is done in related list xml configuration. There is an<br>
	 * entry "quicksearch" with an attribute called "baseEntityAttribute".<br>
	 * The value there is for example "firma", following the example from above.<br>
	 * 
	 * @return the attribute name of the related base entity or null 
	 */
	public String getBaseTypeAttribute() {
		return baseTypeAttribute;
	}

	/**
	 * This method sets the name of the attribute to a related base entity. <p>
	 * 
	 * For example: Unternehmen is opened in editor, below, the list of Kontakte is
	 * displayed. <br>
	 * In that case, this method should set "firma", which is the related attribute<br>
	 * name to the unternehmen.<br><br>
	 * 
	 * The configuration is done in related list xml configuration. There is an<br>
	 * entry "quicksearch" with an attribute called "baseEntityAttribute".<br>
	 * The value there is for example "firma", following the example from above.<br>
	 * 
	 * @param the related base entity attribute name 
	 */
	public void setBaseTypeAttribute(String baseTypeAttribute) {
		this.baseTypeAttribute = baseTypeAttribute;
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
	 * @return the defaultSearchProperties
	 */
	public String getDefaultSearchProperties() {
		return defaultSearchProperties;
	}
	
	/**
	 * @param defaultSearchProperties the defaultSearchProperties to set
	 */
	public void setDefaultSearchProperties(String defaultSearchProperties) {
		this.defaultSearchProperties = defaultSearchProperties;
	}

	/**
	 * @return the depends
	 */
	public String getExtendsID() {
		return extendsID;
	}

	/**
	 * @param depends the depends to set
	 */
	public void setExtendsID(String depends) {
		this.extendsID = depends;
	}

	/**
	 * @return the merged
	 */
	public boolean isMerged() {
		return merged;
	}

	/**
	 * @param merged the merged to set
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}
}
