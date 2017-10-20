/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.news.NewsAdminDialog 
 */
package de.xwic.appkit.webbase.home.news;

import java.util.Collection;
import java.util.Date;

import de.jwic.base.ControlContainer;
import de.jwic.base.Dimension;
import de.jwic.base.Page;
import de.jwic.controls.Button;
import de.jwic.controls.Label;
import de.jwic.controls.StackedContainer;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.AppkitModelConfig;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.INews;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.table.EntityTable;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author lippisch
 *
 */
public class NewsAdminDialog extends AbstractDialogWindow implements INewsEditorHandler {

	private StackedContainer stack;
	private Button btEdit;
	private String appId;
	private EntityListView<INews> entityListView;

	/**
	 * @param site
	 */
	public NewsAdminDialog(Site site, String appId) {
		super(site);
		this.appId = appId;
		
		setCloseable(true);
		setModal(true);
		setTitle("News Administration");
		
		Page page = Page.findPage(site);
		Dimension pageSize = page.getPageSize();
		setWidth((int)(pageSize.width * 0.8d));
		setHeight((int)(pageSize.height * 0.8d));
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {
		stack = new StackedContainer(content, "stack");
		
		ControlContainer newsTable = new ControlContainer(stack, "table");
		
		try {
			EntityListViewConfiguration config = new EntityListViewConfiguration(INews.class);
			
			// load only the news for the current application
			PropertyQuery defaultQuery = new PropertyQuery();
			defaultQuery.addEquals("appId", appId);
			
			config.setBaseFilter(defaultQuery);
			
			entityListView = new NewsEntityListView(newsTable, "table", config);
			
			EntityTable entityTable = entityListView.getEntityTable();
			entityTable.setWidth(getWidth() - 12);
			entityTable.setHeight(getHeight() - 110);
			entityTable.addElementSelectedListener(new ElementSelectedListener() {
				@Override
				public void elementSelected(ElementSelectedEvent event) {
					onTableSelection(event);
					if (event.isDblClick()) {
						onEdit();
					}
				}
			});
			
			ToolBar toolbar = entityListView.getToolbar();
			
			ToolBarGroup group = toolbar.addGroup();
			Button btNew = group.addButton();
			btNew.setTitle("Add News");
			btNew.setIconEnabled(ImageLibrary.IMAGE_ADD);
			btNew.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent event) {
					onAddNews();
				}
			});
			
			btEdit = group.addButton();
			btEdit.setTitle("Edit");
			btEdit.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
			btEdit.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
			btEdit.setEnabled(false);
			btEdit.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent event) {
					onEdit();
				}
			});
			
		} catch (ConfigurationException e) {
			log.error("Error creating News Table", e);
			content.removeControl("stack");
			new Label(content).setText("Error creating table. Contact Support: " + e);
		}		
	}

	/**
	 * @param event
	 */
	protected void onTableSelection(ElementSelectedEvent event) {
		btEdit.setEnabled(event.getElement() != null);
	}

	/**
	 * Edit News.
	 */
	protected void onEdit() {
		
		Collection<String> selection = entityListView.getEntityTable().getTableViewer().getModel().getSelection();
		if (!CollectionUtil.isEmpty(selection)) {
			long id = Long.parseLong(selection.iterator().next());
			INews news = AppkitModelConfig.getNewsDAO().getEntity(id);
			openEditor(news);
		}

	}

	/**
	 * Add News.
	 */
	protected void onAddNews() {
		
		IMitarbeiterDAO maDAO = DAOSystem.getDAO(IMitarbeiterDAO.class);
		INews news = AppkitModelConfig.getNewsDAO().createEntity();
		news.setAuthor(maDAO.buildTitle(maDAO.getByCurrentUser()));
		news.setPublishDate(new Date());
		news.setAppId(appId);
		openEditor(news);
		
	}
	
	/**
	 * Open the editor.
	 * @param news
	 */
	protected void openEditor(INews news) {

		btOk.setVisible(false);
		
		NewsEditor newsEditor = new NewsEditor(stack, "editor", news, this);
		newsEditor.setDimension(getWidth() - 10, getHeight() - 50);
		stack.setCurrentControlName("editor"); // flip to editor
		
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.ui.home.news.INewsEditorHandler#editorDone(boolean)
	 */
	@Override
	public void editorDone(boolean saved) {
		if (saved) {
			entityListView.getEntityTable().getTableViewer().requireRedraw();
		}
		stack.removeControl("editor");
		btOk.setVisible(true);
	}
	
}
