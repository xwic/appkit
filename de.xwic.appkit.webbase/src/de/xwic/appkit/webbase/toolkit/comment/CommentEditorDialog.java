/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.comment;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ButtonControl;
import de.jwic.controls.InputBoxControl;
import de.jwic.controls.LabelControl;
import de.jwic.controls.WindowControl;
import de.jwic.ecolib.controls.ErrorWarningControl;
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
		WindowControl win = new WindowControl(container, "scrollcontainer");
		final ErrorWarningControl errorControl = new ErrorWarningControl(win, "error");
		errorControl.setShowStackTrace(false);
		
		win.setTemplateName(getClass().getName());
		win.setWidth("480");
		win.setAlign("center");
		win.setTitle("Enter Comment for selected Entity");
		
		
		LabelControl lblComment = new LabelControl(win, "lblComment");
		lblComment.setText("Comment");
		
		final InputBoxControl commentControl = new InputBoxControl(win, "comment");
		commentControl.setMultiLine(true);
		commentControl.setRows(10);
		commentControl.setWidth(400);
		
		ButtonControl btFinish = new ButtonControl(win, "Finish");
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
	
		ButtonControl btAbort = new ButtonControl(win, "Abort");
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
