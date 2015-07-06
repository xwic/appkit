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
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.attachment.MultiAttachmentControl;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Ronny Pfretzschner
 *
 */
public class CommentHelperView extends ControlContainer implements IEditorModelListener {

    private Label lbComment, lbAuthor, lbDate;
    private InputBox inpComment;
    private Label fldAuthor, fldDate;
    private MultiAttachmentControl attachments;
    private IEntityComment comment;
    private EditorModel model;
    private Button btEditComment;
    private Button btAddAttachment;
    private boolean isSingleComment;
    private boolean readonlyView = false;
    private boolean attachmentsOnly;

	public CommentHelperView(IControlContainer container, String name, EditorModel model, IEntityComment comment, boolean isSingleComment, boolean attachmentsOnly) {
		super(container, name);
        this.model = model;
        this.comment = comment;
        this.isSingleComment = isSingleComment;
        this.attachmentsOnly = attachmentsOnly;
        this.model.addModelListener(this);
        createControls();
        loadFields();
	}

    private void setReadonlyIfTrue() {
        inpComment.setEnabled(!readonlyView);
        btEditComment.setEnabled(!readonlyView);
        btAddAttachment.setEnabled(!readonlyView);
        
        if(!attachments.isReadonlyView()) {
            attachments.setReadonlyView(readonlyView);
        }
        
        requireRedraw();
    }


	private void createControls() {
        lbComment = new Label(this, "lbComment");
        lbComment.setText("Comment");
        inpComment = new InputBox(this, "inpComment");
        inpComment.setRows(2);
        inpComment.setCols(90);
        inpComment.setCssClass("readonly");
        inpComment.setMultiLine(true);
        inpComment.setReadonly(true);
        inpComment.setWidth(500);

        lbDate = new Label(this, "lbDate");
        lbDate.setText("Date");

        lbAuthor = new Label(this, "lbAuthor");
        lbAuthor.setText("Author");

        fldAuthor = new Label(this, "fldAuthor");
        fldDate = new Label(this, "fldDate");

        attachments = new MultiAttachmentControl(this, "ctlAttachments", model);
        
        btEditComment = new Button(this, "btEditComment");
        //btEditComment.setTemplateName(Button.class.getName() + "_Action");
        btEditComment.setTitle("Edit Comment");
        btEditComment.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
        btEditComment.addSelectionListener(new SelectionListener() {
        	public void objectSelected(SelectionEvent event) {
                Site site = ExtendedApplication.getInstance(CommentHelperView.this).getSite();
                CommentEditorModel commentEditorModel = new CommentEditorModel(comment, model, isSingleComment);
                commentEditorModel.addModelListener(CommentHelperView.this);
                
                CommentEditorDialog editor = new CommentEditorDialog(site, commentEditorModel);
                editor.open();
        	}
        });

        btAddAttachment = new Button(this, "btAddAttachment");
        //btAddAttachment.setTemplateName(Button.class.getName() + "_Action");
        btAddAttachment.setTitle("Add File");
        btAddAttachment.setIconEnabled(ImageLibrary.ICON_NEW_ACTIVE);
        btAddAttachment.setIconDisabled(ImageLibrary.ICON_NEW_INACTIVE);
        btAddAttachment.addSelectionListener(new SelectionListener() {
        	public void objectSelected(SelectionEvent event) {
        		attachments.actionAddAttm();
        		
        	}
        });

        if(attachmentsOnly) {
            btEditComment.setVisible(false);
        }		
	}



	private void loadFields() {
        inpComment.setText(comment.getComment() == null ? "" : comment.getComment());
        fldDate.setText(comment.getCreatedAt().toString());
        fldAuthor.setText(comment.getCreatedFrom());
        if (comment.isReadonlyComment()) {
            btEditComment.setVisible(false);
            btAddAttachment.setVisible(false);
            attachments.setReadonlyView(true);
        }
        attachments.loadAttachmentList(comment);
		
	}



	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
			attachments.saveAttachmentList(comment);
		}
		else if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
	           // dirty, dirty, aber auf die schnelle...
            IControlContainer pce = this.getContainer();
            
            if (pce instanceof CommentEditorControl) {
            	CommentEditorControl con = (CommentEditorControl) pce;
                con.loadFields();
            }
		}
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
		setReadonlyIfTrue();
	}

    public void refresh() {
        inpComment.setText(comment.getComment() != null ? comment.getComment() : "");
        // also redraws attachment control
        requireRedraw();
    }	
    
    public MultiAttachmentControl getAttachmentControl() {
    	return attachments;
    }

	/**
	 * @return the attachmentsOnly
	 */
	public boolean isAttachmentsOnly() {
		return attachmentsOnly;
	}
}
