/**
 *
 */
package de.xwic.appkit.webbase.entityselection;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityview.EntityDisplayListModel;
import de.xwic.appkit.webbase.entityviewer.quickfilter.AbstractQuickFilterPanel;
import de.xwic.appkit.webbase.table.EntityTableModel;

import java.util.Arrays;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public final class GenericEntitySelectionContributor extends EntitySelectionAdapter {
    private final EntitySelectionModel selectionModel;
    private final EntityDisplayListModel listModel;
    private final Class<? extends IEntity> entityType;
    private final String title;
    private final String subTitle;

    /**
     *
     */
    public GenericEntitySelectionContributor(Class<? extends IEntity> entityType, String pageTitle, String pageSubTitle, String... queryProps) {
        this(entityType, new PropertyQuery(), pageTitle, pageSubTitle, queryProps);
    }

    /**
     *
     */
    public GenericEntitySelectionContributor(Class<? extends IEntity> entityType, PropertyQuery pq, String pageTitle, String pageSubTitle, String... props) {
        selectionModel = new EntitySelectionModel();
        selectionModel.setQueryProperties(Arrays.asList(props));
        listModel = new EntityDisplayListModel(pq, entityType);
        this.entityType = entityType;
        this.title = pageTitle;
        this.subTitle = pageSubTitle;

    }

    @Override
    public EntitySelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public EntityDisplayListModel getListModel() {
        return listModel;
    }

    /* (non-Javadoc)
     * @see com.netapp.pulse.basegui.entityselection.EntitySelectionAdapter#getPageTitle()
     */
    @Override
    public String getPageTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see com.netapp.pulse.basegui.entityselection.EntitySelectionAdapter#getPageSubTitle()
     */
    @Override
    public String getPageSubTitle() {
        return subTitle;
    }

	@Override
	public Class<? extends IEntity> getEntityType() {
		return entityType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.netapp.pulse.basegui.entityselection.IEntitySelectionContributor#getQuickFilterPanel(de.jwic.base.IControlContainer, de.xwic.appkit.webbase.table.EntityTableModel)
	 */
	@Override
	public AbstractQuickFilterPanel getQuickFilterPanel(
			IControlContainer container, EntityTableModel tableModel) {
		return null;
	}
}
