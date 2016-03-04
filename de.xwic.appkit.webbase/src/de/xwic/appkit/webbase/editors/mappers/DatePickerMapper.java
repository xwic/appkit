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

import de.jwic.controls.DatePicker;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Mapper for the InputBox control.
 *
 * @author Aron Cotrau
 */
public class DatePickerMapper extends PropertyMapper<DatePicker> {

	public final static String MAPPER_ID = "DatePicker";

	/**
	 * @param baseEntity
	 */
	public DatePickerMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, DatePicker text, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
		if(value == null) {
			value = Calendar.getInstance().getTime();
		}
		if(value instanceof Date) {
			final Date dateValue = (Date) value;
			text.setDate(dateValue);
		} else if(value instanceof String) {
			String dateFormat = text.getDateFormat();
			final String strValue = (String) value;
			try {
				text.setDate(new SimpleDateFormat(dateFormat).parse(strValue));
			} catch (ParseException e) {
				throw new MappingException(e.getMessage(), e);
			}
		} else {
			throw new IllegalArgumentException("Unable to map value. Target type is not Date or String.");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
    @Override
	public void setEditable(DatePicker text, Property[] property, boolean editable) {
		text.setReadonly(!editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
    @Override
	public void storeContent(IEntity entity, DatePicker text, Property[] property) throws MappingException,
			ValidationException {
		writeValue(entity, property, text.getDate());
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
    @Override
	protected void addPropertyToQuery(DatePicker text, Property[] property, IPropertyQuery query) {
		if (text.getText().length() > 0) {
			query.addLikeWithWildcardSetting(getPropertyKey(property), text.getText());
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#highlightWidget(de.jwic.base.IControl, de.xwic.appkit.core.dao.ValidationResult.Severity)
	 */
	@Override
	protected void highlightWidget(DatePicker text, Severity error) {
		if (error == null) {
			text.setFlagAsError(false);
		} else if (error == Severity.ERROR) {
			text.setFlagAsError(true);
		} else {
			text.setFlagAsError(false); // do not flag warnings at this time..
		}
	}
	
}
