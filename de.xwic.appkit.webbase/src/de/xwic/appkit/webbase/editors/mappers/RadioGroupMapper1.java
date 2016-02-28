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

import de.jwic.controls.RadioGroup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.editors.builders.EYesNoRadioBuilder;

/**
 * Mapper for the InputBox control.
 *
 * @author Aron Cotrau
 */
public class RadioGroupMapper1 extends PropertyMapper<RadioGroup> {

	public final static String MAPPER_ID = "BooleanRadioGroup"; 

	/**
	 * @param baseEntity
	 */
	public RadioGroupMapper1(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, RadioGroup text, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
        if(value == null) {
            text.setSelectedKey(EYesNoRadioBuilder.KEY_NO);
            return;
        }
        if(value instanceof Boolean) {
            final Boolean booleanValue = (Boolean) value;
            text.setSelectedKey(booleanValue ? EYesNoRadioBuilder.KEY_YES : EYesNoRadioBuilder.KEY_NO);
        } else {
            throw new IllegalArgumentException("Unable to map value. Target type is not Boolean.");
        }
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
    @Override
	public void setEditable(RadioGroup text, Property[] property, boolean editable) {
        text.setEnabled(editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
    @Override
	public void storeContent(IEntity entity, RadioGroup text, Property[] property) throws MappingException,
			ValidationException {
        final Boolean value = EYesNoRadioBuilder.KEY_YES.equals(text.getSelectedKey());
		writeValue(entity, property, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
    @Override
	protected void addPropertyToQuery(RadioGroup text, Property[] property, IPropertyQuery query) {
		if (text.getSelectedKey().length() > 0) {
			query.addLikeWithWildcardSetting(getPropertyKey(property), text.getSelectedKey());
		}
	}
}
