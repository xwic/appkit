/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.mappers.InputboxMapper
 * Created on Jun 11, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors.mappers;

import de.jwic.base.IControl;
import de.jwic.controls.InputBoxControl;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * Mapper for the InputBox control.
 *
 * @author Aron Cotrau
 */
public class InputboxMapper extends PropertyMapper {

	/**
	 * @param baseEntity
	 */
	public InputboxMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	public void loadContent(IEntity entity, IControl widget, Property[] property) throws MappingException {
		Object value = readValue(entity, property);
		InputBoxControl text = (InputBoxControl) widget;
		text.setText(value != null ? value.toString() : "");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
	public void setEditable(IControl widget, Property[] property, boolean editable) {
		InputBoxControl text = (InputBoxControl) widget;
		text.setReadonly(!editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	public void storeContent(IEntity entity, IControl widget, Property[] property) throws MappingException,
			ValidationException {
		InputBoxControl text = (InputBoxControl) widget;
		writeValue(entity, property, text.getText());
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
	protected void addPropertyToQuery(IControl widget, Property[] property, IPropertyQuery query) {
		InputBoxControl text = (InputBoxControl) widget;
		if (text.getText().length() > 0) {
			query.addLikeWithWildcardSetting(getPropertyKey(property), text.getText());
		}
	}
}
