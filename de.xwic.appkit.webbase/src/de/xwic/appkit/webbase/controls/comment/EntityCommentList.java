/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.controls.comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.IDialogWindowListener;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.utils.StringUtils;

/**
 * @author lippisch
 */
@SuppressWarnings("serial")
public class EntityCommentList extends ControlContainer {

	private static final int DEFAULT_MAX_COMMENTS = 5;
	private String entityType;
	private long entityId;
	private int maxComments = DEFAULT_MAX_COMMENTS;
	private int totalComments = 0;
	private int maxCommentDisplaySize = 300;
	private boolean deleteEnabled = false;
	private boolean wideTemplate = false;
	private boolean allowEdit = false;
	private String category;

	private Map<String, IMitarbeiter> efMap = new HashMap<String, IMitarbeiter>();

	private TimeZone timeZone;

	private List<EditableEntityComment> editableCommentControls;
	
	private DateFormat dfDateTimeWithTimeZone;

	/**
	 * @param container
	 * @param name
	 * @param baseEntity
	 * @param category
	 * @param wideTemplate
	 * @param allowEdit
	 * @param maxComments
	 */
	public EntityCommentList(IControlContainer container, String name, IEntity baseEntity, String category, boolean wideTemplate,
			boolean allowEdit, int maxComments) {

		this(container, name, baseEntity.type().getName(), baseEntity.getId(), category, wideTemplate, allowEdit, maxComments);
	}

	/**
	 * @param container
	 * @param name
	 * @param entityType
	 * @param entityId
	 * @param wideTemplate
	 * @param allowEdit
	 * @param maxComments
	 */
	public EntityCommentList(IControlContainer container, String name, String entityType, long entityId, String category,
			boolean wideTemplate, boolean allowEdit, int maxComments) {
		super(container, name);

		this.entityType = entityType;
		this.entityId = entityId;
		this.wideTemplate = wideTemplate;
		this.allowEdit = allowEdit;
		this.maxComments = maxComments;
		this.category = category;
		
		dfDateTimeWithTimeZone = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa z");
		timeZone = getSessionContext().getTimeZone();
		dfDateTimeWithTimeZone.setTimeZone(timeZone);
		
		init();
	}

	/**
	 * 
	 */
	private void init() {
		timeZone = getSessionContext().getTimeZone();

		if (wideTemplate) {
			setTemplateName(EntityCommentList.class.getName() + "_wideLayout");
			editableCommentControls = new ArrayList<EditableEntityComment>();
			List<IEntityComment> comments = loadComments();
			for (IEntityComment comment : comments) {
				IMitarbeiter employee = getEmployeeFlags(comment.getCreatedFrom());

				EditableEntityComment cmtControl = new EditableEntityComment(this, comment, employee, allowEdit);
				editableCommentControls.add(cmtControl);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getTimeZoneName() {
		return timeZone.getDisplayName();
	}

	/**
	 * 
	 */
	public void actionShowAllComments() {
		AllEntityCommentsDialog cmt = new AllEntityCommentsDialog(ExtendedApplication.getInstance(this).getSite(), entityType, entityId, category,
				allowEdit);
		cmt.show();
		cmt.addDialogWindowListener(new IDialogWindowListener() {

			@Override
			public void onDialogOk(DialogEvent event) {
				requireRedraw();
			}

			@Override
			public void onDialogAborted(DialogEvent event) {
				requireRedraw();
			}
		});
	}

	/**
	 * List the recent comments.
	 * 
	 * @return
	 */
	private List<IEntityComment> loadComments() {

		IEntityCommentDAO ecDAO = DAOSystem.getDAO(IEntityCommentDAO.class);
		PropertyQuery query = new PropertyQuery();
		query.addEquals("entityType", entityType);
		query.addEquals("entityId", entityId);
		query.addEquals("category", category);
		query.setSortField("createdAt");
		query.setSortDirection(PropertyQuery.SORT_DIRECTION_DOWN);
		Limit limit = null;
		if (maxComments != 0) {
			limit = new Limit(0, maxComments);
		}

		EntityList<IEntityComment> el = ecDAO.getEntities(limit, query);
		totalComments = el.getTotalSize();

		return el;

	}

	/**
	 * Returns the EmployeeFlags for the given Mitarbeiter.
	 * 
	 * @param logonName
	 * @return
	 */
	public IMitarbeiter getEmployeeFlags(String logonName) {

		if (efMap.containsKey(logonName)) {
			return efMap.get(logonName);
		}
		IMitarbeiter mitarbeiter = DAOSystem.getDAO(IMitarbeiterDAO.class).getMittarbeiterByUsername(logonName);
		if (mitarbeiter != null) {
			efMap.put(logonName, mitarbeiter);
			return mitarbeiter;
		} else {
			return null;
		}

	}

	/**
	 * @param editableEntityComment
	 */
	public void removeCommentControl(EditableEntityComment editableEntityComment) {
		editableCommentControls.remove(editableEntityComment);
	}

	/**
	 * @return the maxComments
	 */
	public int getMaxComments() {
		return maxComments;
	}

	/**
	 * @return the deleteEnabled
	 */
	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}

	/**
	 * @param deleteEnabled
	 *            the deleteEnabled to set
	 */
	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	/**
	 * @return the totalComments
	 */
	public int getTotalCommentsCount() {
		return totalComments;
	}


	public int getMaxCommentDisplaySize() {
		return maxCommentDisplaySize;
	}

	/**
	 * @param maxCommentDisplaySize
	 *            represents the max length of the comment (in characters) to display before the <br>
	 *            before the "Read All..." button is shown
	 */
	public void setMaxCommentDisplaySize(int maxCommentDisplaySize) {
		this.maxCommentDisplaySize = maxCommentDisplaySize;
	}

	/**
	 * @return the comments
	 */
	public List<IEntityComment> getComments() {
		// load the comments every time to make sure the redraw displays fresh data
		return loadComments();
	}

	/**
	 * @return the editableComentControls
	 */
	public List<EditableEntityComment> getEditableCommentControls() {
		return editableCommentControls;
	}

	/***
	 * @param comment
	 * @return
	 */
	public String parseComment(String comment) {
		if (comment.startsWith("<a href=\"mailto:")) {
			// means this is a comment coming from an email, we don't want to show the whole text
			// see PulseUtil.addCommentToBookingRequestFromEmail() for details
			Pattern pattern = Pattern.compile("<a href=\"mailto:.*>(.*)</a>");
			Matcher matcher = pattern.matcher(comment);
			if (matcher.find()) {
				comment = matcher.group(1) + " replied via email";
			}
		}

		return StringUtils.trimToSize(comment, maxCommentDisplaySize);
	}
	
	/**
	 * @return the vtlHelper
	 */
	public String dateTime(Date date) {
		return dfDateTimeWithTimeZone.format(date);
	}
	
	/**
	 * @param maxComments the maxComments to set
	 */
	public void setMaxComments(int maxComments) {
		this.maxComments = maxComments;
	}
}
