/**
 * 
 */
package de.xwic.appkit.webbase.editors.mappers;

import java.util.List;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.entityviewer.EntityListView;

/**
 * @author lippisch
 */
public class ListViewMapper extends PropertyMapper<EntityListView<IEntity>> {

	public final static String MAPPER_ID = "EntityListView"; 

	/**
	 * @param baseEntity
	 */
	public ListViewMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
	@Override
	public void setEditable(EntityListView<IEntity> widget, Property[] property, boolean editable) {
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, EntityListView<IEntity> listView, Property[] property) throws MappingException {
		
		PropertyQuery baseFilter = listView.getConfiguration().getBaseFilter();
		
		List<QueryElement> elements = baseFilter.getElements();
		if (elements.size() > 0) {
			// the first element in the query must be the filterOn property, which is the reference to the id of the parent. 
			QueryElement filter = elements.get(0); 
			filter.setValue(entity.getId());
		}
		listView.requireRedraw();
		
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void storeContent(IEntity entity, EntityListView<IEntity> widget, Property[] property) throws MappingException, ValidationException {
		
	}

}
