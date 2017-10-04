/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.controls.comment;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.GroupControl;
import de.jwic.controls.InputBox;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;

/**
 * A simple list of EntityComments with a small InputBox to immediately add a comment.
 *
 * @author lippisch
 */
@SuppressWarnings("serial")
public class EntityShoutBox extends GroupControl {

	private enum EventType {
		NEW_COMMENT, ERROR
	}

	private String entityType;
	private long entityId;

	protected InputBox inpComment;
	protected Button btAdd;
	protected EntityCommentList commentList;

	private List<IEntityShoutBoxListener> listeners = new ArrayList<>();
	private IEntity baseEntity;
	private String category;

	/**
	 * @param container
	 * @param name
	 * @param baseEntity
	 * @param category
	 */
	public EntityShoutBox(IControlContainer container, String name, IEntity baseEntity, String category) {
		this(container, name, baseEntity, category, false);
	}

	/**
	 * @param container
	 * @param name
	 * @param baseEntity
	 * @param lockEntityBeforeAdding
	 */
	public EntityShoutBox(IControlContainer container, String name, IEntity baseEntity) {
		this(container, name, baseEntity, false);
	}

	/**
	 * @param container
	 * @param name
	 * @param baseEntity
	 * @param lockEntityBeforeAdding
	 * @param allowEditing
	 */
	public EntityShoutBox(IControlContainer container, String name, IEntity baseEntity, 
			boolean allowEditing) {
		this(container, name, baseEntity, null, allowEditing);
	}

	/**
	 * @param container
	 * @param name
	 * @param baseEntity
	 * @param lockEntityBeforeAdding
	 */
	public EntityShoutBox(IControlContainer container, String name, IEntity baseEntity, String category, boolean allowEditing) {
		super(container, name);
		if (baseEntity == null) {
			throw new NullPointerException("BaseEntity must not be null");
		}

		setTitle("Comments");

		
		this.baseEntity = baseEntity;
		this.category = category;

		entityType = baseEntity.type().getName();
		entityId = baseEntity.getId();

		inpComment = new InputBox(this, "inpComment");
		inpComment.setHeight(50);
		inpComment.setMultiLine(true);
		inpComment.setFillWidth(false);

		btAdd = new Button(this, "btAdd");
		btAdd.setTitle("Add Comment");
		btAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				onAddComment();
			}
		});

		commentList = new EntityCommentList(this, "list", baseEntity, category, false, allowEditing, 5);

		setWidth(300);
	}

	@Override
	public void setEnabled(boolean enabled) {
		inpComment.setEnabled(enabled);
		btAdd.setEnabled(enabled);

	}

	/**
	 * Add a listener.
	 *
	 * @param listener
	 */
	public void addEntityShoutBoxListener(IEntityShoutBoxListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener.
	 *
	 * @param listener
	 */
	public void removeEntityShoutBoxListener(IEntityShoutBoxListener listener) {
		listeners.remove(listener);
	}

	/**
	 *
	 */
	protected IEntityComment onAddComment() {

		IEntityComment comment = null;
		String text = inpComment.getText();
		boolean isLocked = false;
		if (text.trim().length() != 0) {

			IEntityCommentDAO dao = DAOSystem.getDAO(IEntityCommentDAO.class);
			comment = dao.createEntity();

			comment.setComment(text);
			comment.setEntityId(entityId);
			comment.setEntityType(entityType);
			comment.setCategory(category);
			dao.update(comment);

			// notify listeners
			fireEvent(EventType.NEW_COMMENT, new EntityShoutBoxEvent(this, text));

			inpComment.setText("");
			commentList.requireRedraw();
		}

		return comment;
	}

	/**
	 * @param new_comment
	 * @param entityShoutBoxEvent
	 */
	private void fireEvent(EventType eventType, EntityShoutBoxEvent event) {
		fireEvent(eventType, event, "");
	}

	/**
	 * @param error
	 * @param entityShoutBoxEvent
	 * @param message
	 */
	private void fireEvent(EventType eventType, EntityShoutBoxEvent event, String errorMessage) {
		IEntityShoutBoxListener lst[] = new IEntityShoutBoxListener[listeners.size()];
		lst = listeners.toArray(lst);

		for (IEntityShoutBoxListener listener : lst) {
			switch (eventType) {
			case NEW_COMMENT:
				listener.commentCreated(event);
				break;
			case ERROR:
				listener.errorOccured(event, errorMessage);
				break;
			}
		}
	}

	/**
	 * @param maxWidth
	 *            the maxWidth to set
	 */
	@Override
	public void setWidth(int maxWidth) {
		super.setWidth(maxWidth);
		inpComment.setWidth(maxWidth - 32);
	}

	/**
	 * @return the commentList
	 */
	public EntityCommentList getCommentList() {
		return commentList;
	}

	/**
	 * @param commentList
	 *            the commentList to set
	 */
	public void setCommentList(EntityCommentList commentList) {
		this.commentList = commentList;
	}

	/**
	 * Refreshes the id of given base entity.
	 */
	public void refreshEntityId() {
		entityId = baseEntity.getId();
	}
}
