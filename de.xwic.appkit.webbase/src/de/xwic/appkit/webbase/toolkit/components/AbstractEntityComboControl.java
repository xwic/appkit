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
import de.jwic.controls.ListBox;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Created on 01.04.2008
 * @author Ronny Pfretzschner
 */
public abstract class AbstractEntityComboControl<E extends IEntity> extends ListBox implements IEntityListBoxControl<E> {

	protected boolean allowEmptySelection = true;


	public AbstractEntityComboControl(IControlContainer container, String name) {
		super(container, name);
		setTemplateName(ListBox.class.getName());
	}


	public abstract DAO<E> getEntityDao();

	protected abstract void setupEntries();

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#selectEntry(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void selectEntry(E entity){
		if (entity != null) {
			setSelectedKey(Integer.toString(entity.getId()));
		}
		else {
			setSelectedKey("0");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#getSelectedEntry()
	 */
	@Override
	public E getSelectedEntry(){
		String selectedKey = getSelectedKey();
		if (selectedKey == null || selectedKey.isEmpty()) {
			return null;
		}

		int i = Integer.parseInt(selectedKey);

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
