/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.controls.actions.Action;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public abstract class AbstractEntityAction extends Action implements IEntityAction {

	protected final Log log = LogFactory.getLog(getClass());

	protected String id;
	protected DAO entityDao;
	protected IEntityProvider entityProvider;
	protected Site site;

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityAction#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityAction#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.extensions.actions.IEntityAction#setEntityDao(de.xwic.appkit.core.dao.DAO)
	 */
	@Override
	public void setEntityDao(DAO entityDao) {
		this.entityDao = entityDao;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.extensions.actions.IEntityAction#setEntityProvider(com.netapp.pulse.basegui.extensions.actions.IEntityProvider)
	 */
	@Override
	public void setEntityProvider(IEntityProvider entityProvider) {
		this.entityProvider = entityProvider;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.extensions.actions.IEntityAction#setSite(de.xwic.appkit.webbase.toolkit.app.Site)
	 */
	@Override
	public void setSite(Site site) {
		this.site = site;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityAction#updateState(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void updateState(IEntity entity) {
		setEnabled(entityProvider.hasEntity());
	}
}
