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

import de.jwic.controls.ckeditor.CKEditor;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * Mapper for the CKEditor control.
 *
 * @author Aron Cotrau
 */
public class HtmlEditorMapper extends PropertyMapper<CKEditor> {

	public final static String MAPPER_ID = "HTMLEditor";

	/**
	 * @param baseEntity
	 */
	public HtmlEditorMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, CKEditor text, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
		text.setText(value != null ? value.toString() : "");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
    @Override
	public void setEditable(CKEditor text, Property[] property, boolean editable) {
		text.setEnabled(editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
    @Override
	public void storeContent(IEntity entity, CKEditor text, Property[] property) throws MappingException,
			ValidationException {
		writeValue(entity, property, text.getText());
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
    @Override
	protected void addPropertyToQuery(CKEditor text, Property[] property, IPropertyQuery query) {
		if (text.getText().length() > 0) {
			query.addLikeWithWildcardSetting(getPropertyKey(property), text.getText());
		}
	}
	
}
