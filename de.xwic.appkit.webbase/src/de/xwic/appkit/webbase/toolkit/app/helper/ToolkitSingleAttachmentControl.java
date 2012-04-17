/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
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
public class ToolkitSingleAttachmentControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	public IControl create(IControlContainer container, String name,
			Object optionalParam) {
		SingleAttachmentControl con = new SingleAttachmentControl(container, name, (EditorModel)optionalParam);
		return con;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		IAnhang value = null;
		if (control instanceof SingleAttachmentControl) {
			SingleAttachmentControl sac = (SingleAttachmentControl) control;
			IAttachmentWrapper wrapper = sac.saveAttachment();
			if (wrapper == null)
				value = null;
			else if (wrapper.isDeleted()) {
				value = null;
			} else {
				value = wrapper.getAnhang();
			}
		}
		
		return value;
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
		if (control instanceof SingleAttachmentControl) {
			SingleAttachmentControl sac = (SingleAttachmentControl) control;
			sac.loadAttachment((IAnhang) obj);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {

	}

}
