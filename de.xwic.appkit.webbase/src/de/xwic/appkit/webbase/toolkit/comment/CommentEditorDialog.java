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

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.controls.Window;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.components.Dialog;

/**
 * @author Ronny Pfretzschner
 *
 */
public class CommentEditorDialog extends Dialog {

	private CommentEditorModel commentEditorModel;
	
	public CommentEditorDialog(Site site, CommentEditorModel commentEditorModel) {
		super(site);
		this.commentEditorModel = commentEditorModel;
	}

	@Override
	public void createControls(IControlContainer container) {
		Window win = new Window(container, "scrollcontainer");
		final ErrorWarning errorControl = new ErrorWarning(win, "error");
		errorControl.setShowStackTrace(false);
		
		win.setTemplateName(getClass().getName());
		win.setWidth(480);
		win.setTitle("Enter Comment for selected Entity");
		
		
		Label lblComment = new Label(win, "lblComment");
		lblComment.setText("Comment");
		
		final InputBox commentControl = new InputBox(win, "comment");
		commentControl.setMultiLine(true);
		commentControl.setRows(10);
		commentControl.setWidth(400);
		
		Button btFinish = new Button(win, "Finish");
		btFinish.setTitle("Finish");
		btFinish.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				String comment = commentControl.getText();
				
				if (comment == null || comment.length() < 1) {
					errorControl.showError("You have to enter a comment.");
					return;
				}
				
				IEntityComment prComment = (IEntityComment) commentEditorModel.getComment();
		        prComment.setComment(commentControl.getText());
		        commentEditorModel.saveEntity();
		        commentEditorModel.close();
				finish();
			}
		});
	
		Button btAbort = new Button(win, "Abort");
		btAbort.setTitle("Abort");
		btAbort.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				commentEditorModel.abort();
				abort();
			}
		});
		btAbort.setEnabled(true);
		
		//load field, if possible
		IEntityComment comment = commentEditorModel.getComment();
		
		if (comment != null) {
			commentControl.setText(comment.getComment() != null ? comment.getComment() : "");
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
