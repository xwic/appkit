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
package de.xwic.appkit.webbase.editors.mappers;

import java.util.HashSet;
import java.util.Set;

import de.jwic.base.IControl;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.utils.picklist.IPicklistEntryControl;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryRadioGroupControl;

/**
 * Maps a Set of IPicklistEntry values to multi-selection IPicklistEntryCheckboxControl field.
 * 
 * @author lippisch
 */
public class PicklistEntrySetMapper extends PropertyMapper {

	/**
	 * @param baseEntity
	 */
	public PicklistEntrySetMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	public void loadContent(IEntity entity, IControl widget, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
		
		Set<IPicklistEntry> entries = null;
		if (value instanceof Set) {
			entries = (Set<IPicklistEntry>)value;
		}
		
		PicklistEntryCheckboxControl pe = (PicklistEntryCheckboxControl)widget;
		pe.selectEntries(entries);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
	public void setEditable(IControl widget, Property[] property, boolean editable) {
		
		PicklistEntryCheckboxControl pe = (PicklistEntryCheckboxControl)widget;
		pe.setEnabled(editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	public void storeContent(IEntity entity, IControl widget, Property[] property) throws MappingException,
			ValidationException {
		
		PicklistEntryCheckboxControl pe = (PicklistEntryCheckboxControl)widget;
		Set<IPicklistEntry> entries = new HashSet<IPicklistEntry>();
		entries.addAll(pe.getSelectedEntries());
		writeValue(entity, property, entries);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
	protected void addPropertyToQuery(IControl widget, Property[] property, IPropertyQuery query) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#clearHighlightWidget(de.jwic.base.IControl)
	 */
	@Override
	protected void clearHighlightWidget(IControl widget) {
		// NOT SUPPORTED AT THIS TIME.
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#highlightWidget(de.jwic.base.IControl, de.xwic.appkit.core.dao.ValidationResult.Severity)
	 */
	@Override
	protected void highlightWidget(IControl widget, Severity error) {
		// NOT SUPPORTED AT THIS TIME.
	}
	
}
