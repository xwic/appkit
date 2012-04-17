/*
 * $Id: AbstractEntityComboControl.java,v 1.1 2008/10/10 14:22:30 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.entityselection.AbstractEntityComboControl.java
 * Created on 01.04.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ListBoxControl;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Created on 01.04.2008
 * @author Ronny Pfretzschner
 */
public abstract class AbstractEntityComboControl extends ListBoxControl implements
		IEntityListBoxControl {

	protected boolean allowEmptySelection = true;

	
	public AbstractEntityComboControl(IControlContainer container, String name) {
		super(container, name);
		setTemplateName(ListBoxControl.class.getName());
	}

	
	public abstract DAO getEntityDao();
	
	protected abstract void setupEntries();
	
	/** select the item corresponding to the given entry id
	 * @param entity
	 */
	public void selectEntry(IEntity entity){
		if (entity != null) {
			setSelectedKey(Integer.toString(entity.getId()));
		}
		else {
			setSelectedKey("0");
		}
	} 
	
	/**
	 * @return the Entity of the selected entry
	 */
	public IEntity getSelectedEntry(){
		if (getSelectedKey() == null || getSelectedKey().length() < 1) {
			return null;
		}
		
		int i = Integer.parseInt(getSelectedKey());
		
		if (i > 0){	
			return getEntityDao().getEntity(i);
		}
		return null;
	}

	
	/**
	 * If true, use can select "empty" value.
	 * @param allowEmptySelection
	 */
	public void setAllowEmptySelection(boolean allowEmptySelection) {
		this.allowEmptySelection = allowEmptySelection;
	}
	
}
