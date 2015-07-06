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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.comment;

import java.util.Date;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.core.model.entities.impl.EntityComment;
import de.xwic.appkit.webbase.toolkit.editor.EditorEventSupport;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;

/**
 * @author Ronny Pfretzschner
 *
 */
public class CommentEditorModel extends EditorEventSupport {

    private EditorModel model;
    private IEntityComment comment;

    private String userName;
    
    private boolean isSingleComment;

    /**
     * Default setting, Editor should be closed after save.
	 * This flag is used on GUI to check this.
     * 
     */
    private boolean doCloseAfterSave = true;

    /**
     * 
     * @param comment
     * @param model
     * @param isSingleComment
     */
    public CommentEditorModel(IEntityComment comment, EditorModel model, boolean isSingleComment) {
        this.model = model;
        this.comment = comment;
        this.userName = DAOSystem.getSecurityManager().getCurrentUser().getName();
        this.isSingleComment = isSingleComment;
    }


    public CommentEditorModel(IEntityComment comment, EditorModel model, boolean isSingleComment, boolean doCloseAfterSave) {
        this.model = model;
        this.comment = comment;
        this.isSingleComment = isSingleComment;
        this.doCloseAfterSave = doCloseAfterSave;
    }


    /**
	 * @return the comment
	 */
	public IEntityComment getComment() {
		return comment;
	}


	/**
     * 
     * @return
     */
    public boolean doCloseAfterSave() {
        return doCloseAfterSave;
    }

    public void saveEntity() {
        if (!model.getComments().contains(comment)) {
            // set user and Date here!
    		String usr = (userName == null || userName.length() < 1) ? "UNKNOWN" : userName;
    		EntityComment c = (EntityComment) comment;
       	 
        	if (comment.getId() < 1) {
        		c.setCreatedAt(new Date());
        		c.setCreatedFrom(usr);
        	}
        	else {
        		c.setLastModifiedAt(new Date());
        		c.setLastModifiedFrom(usr);
        	}
        	
            if (!isSingleComment) {
                comment.setEntityId(model.getEntity(false).getId());
                comment.setEntityType(model.getEntity(false).type().getName());
            }
            model.getComments().add(comment);
        }
    }

    public void close() {
    	onCloseRequest(this);
    }

    public void abort() {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.ABORT, this);
    	fireModelChangedEvent(event);    }
}
