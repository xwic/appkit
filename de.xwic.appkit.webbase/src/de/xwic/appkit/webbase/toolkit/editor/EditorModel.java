/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.webbase.toolkit.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.xwic.appkit.core.dao.*;
import de.xwic.appkit.core.file.uc.AttachmentUseCase;
import de.xwic.appkit.core.file.uc.AttachmentWrapper;
import de.xwic.appkit.core.file.uc.CommentUseCase;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.core.model.daos.IAnhangDAO;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * Editormodel for entity editors.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class EditorModel extends EditorEventSupport {

	
    protected ValidationResult lastValidationResult;

    protected List<IAttachmentWrapper> attachments;
    
    protected List<IEntityComment> comments;
    
    protected IEntity baseEntity;
    protected IEntity entity;

	private boolean editMode = false;
    
	private boolean isEditable = false;
	
    /**
     * 
     * @param entity
     * @param daoSupport
     */
    public EditorModel(IEntity entity, boolean isEditable) {
        this.entity = entity;
        this.isEditable = isEditable;
        attachments = new ArrayList<IAttachmentWrapper>();
        comments = new ArrayList<IEntityComment>();
    }
    
    /**
     * 
     * @param entity
     * @param daoSupport
     */
    public EditorModel(IEntity entity) {
    	this(entity, true);
    }
    

    /**
     * @return the DAO of the entity type behind this model
     */
    public DAO getEntityDAO() {
    	return DAOSystem.findDAOforEntity(entity.type());
    }
    
    public void abort() {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.ABORT, this);
    	fireModelChangedEvent(event);
    }

    /**
     * Close the model.
     */
    public void close() {
    	onCloseRequest(this);
    }

    /**
     * Validates the entity of the model.
     * 
     * @return Validation Result containig warnings or error.
     * 
     * @throws EntityValidationException
     */
    public ValidationResult validateEntity() throws ValidationException {
        lastValidationResult = new ValidationResult();
        onValidationRequest(this);

        DAO dao = DAOSystem.findDAOforEntity(entity.type());
        lastValidationResult = dao.validateEntity(entity);

        onAfterValidationRefreshRequest(this);
        // Disatvantage, if Editortoolkit markInvalid Field-Property is set to true: You could get some Exceptions twice! 
        // One from the validateEntity-Function,
        // if value is null and has to be validate and one from the editor toolkit, that an value
        // has a wrong format!
        if (lastValidationResult.hasErrors()) {
        	ValidationException exception = new ValidationException(lastValidationResult);
            throw exception;
        }
        return lastValidationResult;
    }

    /**
     * Saves the entity
     */
    public void saveEntity() {
    	onValidationRequest(this);
        DAO dao = DAOSystem.findDAOforEntity(entity.type());
        dao.update(entity);
        onAfterSave(this);
    }

	/**
	 * @param true means reload from DB.
	 * @return Returns the entity.
	 */
	public IEntity getEntity(boolean refresh) {
		if (refresh) {
			entity = (entity != null && entity.getId() > 0) ? getEntityDAO().getEntity(entity.getId()) : entity;
		}
		return entity;
	}

	/**
	 * @return the entity hold in the model.
	 */
	public IEntity getEntity() {
		return getEntity(false);
	}
	
	/**
	 * 
	 * @return the attachments of the entity
	 */
	public List<IAttachmentWrapper> getAttachments() {
		return attachments;
	}

	/**
	 * 
	 * @param attachments for the entity
	 */
	public void setAttachments(List<IAttachmentWrapper> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @param entity The entity to set.
	 */
	public void setEntity(IEntity entity) {
		this.entity = entity;
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.ENTITY_CHANGED, this);
    	fireModelChangedEvent(event);
		
	}

	
	/**
	 * @return the editMode
	 */
	public boolean isEditMode() {
		return editMode;
	}




	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.EDIT_MODE_CHANGED, this);
    	fireModelChangedEvent(event);
		
	}

	
    public List<IEntityComment> getCommentsByEntity(IEntity entity) {
        if (entity == null) {
        	return new ArrayList<IEntityComment>();
        }
    	
    	IEntityCommentDAO commentDAO = (IEntityCommentDAO) DAOSystem.getDAO(IEntityCommentDAO.class);
       	PropertyQuery query = new PropertyQuery();
        query.addEquals("entityType", entity.type().getName());
        query.addEquals("entityId", entity.getId());

        EntityList list = commentDAO.getEntities(null, query);
        
        for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			IEntityComment com = (IEntityComment) iterator.next();
			comments.add(com);
		}

        return comments;
    }

    public List<IAttachmentWrapper> getAttachmentsByEntity(IEntity entity) {
    	IAnhangDAO ahDAO = (IAnhangDAO) DAOSystem.getDAO(IAnhangDAO.class);
    	List<IAttachmentWrapper> attachments = new ArrayList<IAttachmentWrapper>();

    	//new entity! no attachments possible!
    	if (entity == null || entity.getId() < 1) {
    		return attachments;
    	}
    	
    	PropertyQuery query = new PropertyQuery();
        query.addEquals("entityType", entity.type().getName());
        query.addEquals("entityID", entity.getId());
        
        EntityList list = ahDAO.getEntities(null, query);
        int attmCount = 0;
        
        for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			IAnhang ah = (IAnhang) iterator.next();

			AttachmentWrapper attm = new AttachmentWrapper("atm" + (++attmCount), ah);
            attm.setEntity(entity);
            attachments.add(attm);
            this.attachments.add(attm);
		}
        return attachments;
    }

	/**
	 * @return the comments
	 */
	public List<IEntityComment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<IEntityComment> comments) {
		this.comments = comments;
	}

	/**
	 * Manual saving of comments and attachments!
	 */
	public void saveCommentsAndAttachments() {
		onValidationRequest(this);
		AttachmentUseCase att = new AttachmentUseCase(getAttachments(), DAOSystem.getFileHandler());
		CommentUseCase com = new CommentUseCase(getComments());
		//this is not a good solution because it is not
		//save for transactions, normally, a new usecase, calling these two ucs must be created!
		DAOProvider p = ((AbstractDAO)DAOSystem.getDAO(IAnhangDAO.class)).getProvider();
		p.execute(com);
		p.execute(att);
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
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
	 * @return true if the edited entity is new, false otherwise
	 */
	public boolean isNewEntity() {
		return getEntity().getId() <= 0;
	}
}
