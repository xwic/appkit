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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SingleCommentEditorControl extends ControlContainer implements IEditorModelListener {

    private EditorModel model;
    private IEntityComment comment;
    private CommentHelperView helper;
    private boolean readonlyView = false;
    private boolean attachmentsOnly;
	
	public SingleCommentEditorControl(IControlContainer container, String name, EditorModel model, boolean attachmentsOnly) {
		super(container, name);
		
        this.model = model;
        this.attachmentsOnly = attachmentsOnly;
	}

	/**
	 * @return the readonlyView
	 */
	public boolean isReadonlyView() {
		return readonlyView;
	}

	/**
	 * @param readonlyView the readonlyView to set
	 */
	public void setReadonlyView(boolean readonlyView) {
		this.readonlyView = readonlyView;
		
		if (helper != null) {
			helper.setReadonlyView(readonlyView);
			requireRedraw();
		}
	}

	/**
	 * Load the comments.
	 * 
	 * @param comment
	 */
	public void loadComment(IEntityComment comment) {
        if (comment != null && helper == null) {
            this.comment = comment;
            helper = new CommentHelperView(this, "comment", model, this.comment, true, true);
        }
	}
	
    public void actionAddComment(String key) {
        addComment();
    }

    private void addComment() {
        IEntityCommentDAO commentDAO = (IEntityCommentDAO) DAOSystem.getDAO(IEntityCommentDAO.class);
        comment = (IEntityComment)commentDAO.createEntity();
        CommentEditorModel commentEditorModel = new CommentEditorModel(comment, model, true);
        
        if (!attachmentsOnly) {
             
            commentEditorModel.addModelListener(this);
            Site site = ExtendedApplication.getInstance(this).getSite();
            CommentEditorDialog editor = new CommentEditorDialog(site, commentEditorModel);
            editor.open();
        }else {
            commentEditorModel.saveEntity();
            helper = new CommentHelperView(this, "comment", model, comment, true, true);
            helper.getAttachmentControl().actionAddAttm();
        }
    }

	public void modelContentChanged(EditorModelEvent event) {
		if (event.getSource() instanceof CommentEditorModel) {
			if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
				helper = new CommentHelperView(this, "comment", model, comment, true, true);
			}
			else if (EditorModelEvent.ABORT == event.getEventType()) {
				comment = null;
			}
			requireRedraw();
		}
	}

	/**
	 * @return the comment
	 */
	public IEntityComment getComment() {
		return comment;
	}
	
	public boolean hasComment() {
		return comment != null;
	}
}
