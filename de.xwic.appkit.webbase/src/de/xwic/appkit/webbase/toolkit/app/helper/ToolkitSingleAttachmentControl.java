/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.attachment.SingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitSingleAttachmentControl implements IToolkitControlHelper<SingleAttachmentControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	@Override
	public SingleAttachmentControl create(IControlContainer container, String name,
			Object optionalParam) {
		return new SingleAttachmentControl(container, name, (EditorModel) optionalParam);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(SingleAttachmentControl control) {
		IAnhang value = null;
		IAttachmentWrapper wrapper = control.saveAttachment();
		if (wrapper != null && !wrapper.isDeleted()) {
			value = wrapper.getAnhang();
		}

		return value;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(SingleAttachmentControl control) {
		return "";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(SingleAttachmentControl control, Object obj) {
		control.loadAttachment((IAnhang) obj);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(SingleAttachmentControl control, String cssClass) {

	}

}
