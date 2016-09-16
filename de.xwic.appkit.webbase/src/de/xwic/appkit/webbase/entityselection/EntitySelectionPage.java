package de.xwic.appkit.webbase.entityselection;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityview.CloseableEntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.entityviewer.quickfilter.AbstractQuickFilterPanel;
import de.xwic.appkit.webbase.entityviewer.quickfilter.IQuickFilterPanelCreator;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.model.IModelListener;
import de.xwic.appkit.webbase.toolkit.model.ModelEvent;

/**
 * 
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EntitySelectionPage<I extends IEntity> extends InnerPage implements IModelListener {

	/**
	 * @param container
	 * @param name
	 */
	public EntitySelectionPage(IControlContainer container, String name, final IEntitySelectionContributor contributor) {
		super(container, name);
		if (null == contributor.getSelectionModel()) {
			throw new IllegalArgumentException("Selection Model is not allowed to be null");
		}
		
		if (null == contributor.getListModel()) {
			throw new IllegalArgumentException("List Model is not allowed to be null");
		}
		
		contributor.getSelectionModel().addModelListener(this);
		setTitle(contributor.getPageTitle());
		setSubtitle(contributor.getPageSubTitle());
	
		EntityListViewConfiguration config = new EntityListViewConfiguration(contributor.getEntityType());
		EntityQuery originalQuery = contributor.getListModel().getOriginalQuery();
		if (originalQuery instanceof PropertyQuery) {
			config.setBaseFilter((PropertyQuery) originalQuery);
		}
		config.setQuickFilterPanelCreator(new IQuickFilterPanelCreator() {
			
			@Override
			public AbstractQuickFilterPanel createQuickFilterPanel(IControlContainer container, EntityTableModel tableModel) {
				return contributor.getQuickFilterPanel(container, tableModel);
			}
		});
		try {
			new CloseableEntityListView<IEntity>(this, name, config, contributor);
		} catch (ConfigurationException e) {
			throw new IllegalArgumentException("Cannot build entity list view " + e, e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.model.IModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.model.ModelEvent)
	 */
	@Override
	public void modelContentChanged(ModelEvent event) {
		if (EntitySelectionModel.CLOSE_ENTITY_SELECTION == event.getEventType()) {
			// someone wants to close this
			Site site = ExtendedApplication.getInstance(this).getSite();
			site.popPage(this);
		} 
	}

}
