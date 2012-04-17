/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.entityviewer.quickfilter.IQuickFilterPanelCreator;
import de.xwic.appkit.webbase.table.EntityTableConfiguration;

/**
 * @author Adrian Ionescu
 */
public class EntityListViewConfiguration extends EntityTableConfiguration {

	private IEntity baseEntity = null;
	private IQuickFilterPanelCreator quickFilterPanelCreator; 
	
	/**
	 * 
	 */
	public EntityListViewConfiguration(Class<? extends IEntity> entityClass) {
		super(entityClass);
	}
	
	/**
	 * @return the baseEntity
	 */
	public IEntity getBaseEntity() {
		return baseEntity;
	}
	/**
	 * @param baseEntity the baseEntity to set
	 */
	public void setBaseEntity(IEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

	/**
	 * @return the quickFilterPanelCreator
	 */
	public IQuickFilterPanelCreator getQuickFilterPanelCreator() {
		return quickFilterPanelCreator;
	}

	/**
	 * @param quickFilterPanelCreator the quickFilterPanelCreator to set
	 */
	public void setQuickFilterPanelCreator(IQuickFilterPanelCreator quickFilterPanelCreator) {
		this.quickFilterPanelCreator = quickFilterPanelCreator;
	}
}
