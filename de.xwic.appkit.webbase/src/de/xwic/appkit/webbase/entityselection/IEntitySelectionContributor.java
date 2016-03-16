package de.xwic.appkit.webbase.entityselection;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.entityview.EntityDisplayListModel;
import de.xwic.appkit.webbase.entityviewer.quickfilter.AbstractQuickFilterPanel;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * 
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IEntitySelectionContributor {

	public String getPageTitle();
	
	public String getPageSubTitle();
	
	public String getViewTitle();
	
	public String getListSetupId();
	
	public EntitySelectionModel getSelectionModel();
	
	public EntityDisplayListModel getListModel();
	
	public Class<? extends IEntity> getEntityType();
	
	public AbstractQuickFilterPanel getQuickFilterPanel(IControlContainer container, EntityTableModel tableModel);
}
