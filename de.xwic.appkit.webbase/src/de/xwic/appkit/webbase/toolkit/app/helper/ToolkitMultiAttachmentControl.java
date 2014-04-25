/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.comment.SingleCommentEditorControl;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitMultiAttachmentControl implements IToolkitControlHelper<SingleCommentEditorControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	@Override
	public SingleCommentEditorControl create(IControlContainer container, String name,
			Object optionalParam) {
		return new SingleCommentEditorControl(container, name, (EditorModel) optionalParam, true);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(SingleCommentEditorControl control) {
		return control.getComment();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(SingleCommentEditorControl control) {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(SingleCommentEditorControl control, Object obj) {
		control.loadComment((IEntityComment) obj);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(SingleCommentEditorControl control, String cssClass) {

	}

}
