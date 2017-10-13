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

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.editors.controls.UserRolesEditorControl;

/**
 * Mapper for the UserRolesEditor control.
 *
 */
public class UserRolesMapper extends PropertyMapper<UserRolesEditorControl> {

	public final static String MAPPER_ID = "UserRoles"; 
	
	/**
	 * @param baseEntity
	 */
	public UserRolesMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, UserRolesEditorControl ure, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
		ure.setLogonName(value != null ? value.toString() : null);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
    @Override
	public void setEditable(UserRolesEditorControl ure, Property[] property, boolean editable) {
		ure.setEnabled(editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
    @Override
	public void storeContent(IEntity entity, UserRolesEditorControl text, Property[] property) throws MappingException,
			ValidationException {


    	
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
    @Override
	protected void addPropertyToQuery(UserRolesEditorControl text, Property[] property, IPropertyQuery query) {
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#highlightWidget(de.jwic.base.IControl, de.xwic.appkit.core.dao.ValidationResult.Severity)
	 */
	@Override
	protected void highlightWidget(UserRolesEditorControl text, Severity error) {
		
	}
	
}
