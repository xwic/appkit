/**
 * 
 */
package de.xwic.appkit.webbase.editors.mappers;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.editors.controls.AttachmentsControl;

/**
 * @author lippisch
 */
public class AttachmentsMapper extends PropertyMapper<AttachmentsControl> {

	public final static String MAPPER_ID = "AttachmentsWrapper"; 

	/**
	 * @param baseEntity
	 */
	public AttachmentsMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
	@Override
	public void setEditable(AttachmentsControl widget, Property[] property, boolean editable) {
		widget.setReadonlyView(!editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, AttachmentsControl widget, Property[] property) throws MappingException {
		widget.loadAttachmentList(entity);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void storeContent(IEntity entity, AttachmentsControl widget, Property[] property) throws MappingException, ValidationException {
		// there is nothing to be written back to the entity. The files in the attachments control
		// will be saved AFTER the entity has been saved.
	}

	
}
