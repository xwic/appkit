/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.comment.SingleCommentEditorControl;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitMultiAttachmentControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	public IControl create(IControlContainer container, String name,
			Object optionalParam) {
		SingleCommentEditorControl con = new SingleCommentEditorControl(container, name, (EditorModel)optionalParam, true);
		return con;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		IEntityComment comment = null;
		if (control instanceof SingleCommentEditorControl) {
			SingleCommentEditorControl sac = (SingleCommentEditorControl) control;
			comment = sac.getComment();
		}
		
		return comment;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	public String getFieldMarkedCssClass(IControl control) {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	public void loadContent(IControl control, Object obj) {
		if (control instanceof SingleCommentEditorControl) {
			SingleCommentEditorControl sac = (SingleCommentEditorControl) control;
			sac.loadComment((IEntityComment) obj);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {

	}

}
