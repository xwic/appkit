/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.NewsFeed 
 */
package de.xwic.appkit.webbase.home;

import java.util.Date;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.AnchorLink;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.AppkitModelConfig;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.INews;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.core.util.DateFormatter;
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.IDialogWindowListener;
import de.xwic.appkit.webbase.home.news.NewsAdminDialog;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;

/**
 * This is the news feed container.
 * This control displays all the news that are active and visible
 * for the current date.
 * 
 * @author lippisch
 */
public class NewsFeed extends ControlContainer {

	private String appId;
	
	/**
	 * @param container
	 * @param name
	 * @param appId 		application ID.
	 */
	public NewsFeed(IControlContainer container, String name, String appId) {
		super(container, name);
		this.appId = appId;
		
		// only some users can enter news
		AnchorLink btAdmin = new AnchorLink(this, "btAdmin");
		btAdmin.setVisible(DAOSystem.getSecurityManager().hasRight(INews.class.getName(), ApplicationData.SECURITY_ACTION_UPDATE));
		btAdmin.setTitle("News Administration");
		btAdmin.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				openNewsAdmin();
			}
		});
	}

	
	/**
	 * Open the NewsAdmin Dialog.
	 */
	protected void openNewsAdmin() {
		
		NewsAdminDialog naDlg = new NewsAdminDialog(ExtendedApplication.getInstance(this).getSite(), appId);
		naDlg.addDialogWindowListener(new IDialogWindowListener() {
			@Override
			public void onDialogOk(DialogEvent event) {
				requireRedraw();
			}
			@Override
			public void onDialogAborted(DialogEvent event) {
				requireRedraw();
				
			}
		});
		naDlg.show();
		
	}


	/**
	 * Returns the news to be displayed.
	 * @return
	 */
	public List<INews> getNewsToDisplay() {
		Date now = new Date();
		// news that have publish date in the future
		// of visible until in the past
		// do not show up in the news feed
		// News that are active for current date but are not flagged as visible are also hidden
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("visible", true);
		
		PropertyQuery pqFrom = new PropertyQuery();
		pqFrom.addEquals("publishDate", null);
		pqFrom.addQueryElement(new QueryElement(QueryElement.OR, "publishDate", QueryElement.LOWER_EQUALS_THEN, now));
		pq.addSubQuery(pqFrom);
		
		PropertyQuery pqTo = new PropertyQuery();
		pqTo.addEquals("visibleUntil", null);
		pqTo.addQueryElement(new QueryElement(QueryElement.OR, "visibleUntil", QueryElement.GREATER_EQUALS_THEN, now));
		pq.addSubQuery(pqTo);
		
		pq.setSortField("publishDate");
		pq.setSortDirection(PropertyQuery.SORT_DIRECTION_DOWN);
		
		pq.addEquals("appId", appId);		
		
		return AppkitModelConfig.getNewsDAO().getEntities(new Limit(0, 4), pq);
		
	}
	
	/**
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		if(date == null) {
			return "";
		}
		
		return DateFormatter.getDateFormat().format(date);
	}
	
}
