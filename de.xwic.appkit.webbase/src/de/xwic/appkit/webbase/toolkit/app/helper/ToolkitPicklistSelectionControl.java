/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitPicklistSelectionControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	public IControl create(IControlContainer container, String name, Object optionalParam) {
		PicklistEntryControl control = new PicklistEntryControl(container, name, (String)optionalParam);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		if (control instanceof PicklistEntryControl) {
			PicklistEntryControl ibcCon = (PicklistEntryControl) control;
			return ibcCon.getSelectionEntry();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	public void loadContent(IControl control, Object obj) {
		if (control instanceof PicklistEntryControl) {
			PicklistEntryControl ibcCon = (PicklistEntryControl) control;
			ibcCon.selectEntry((IPicklistEntry)obj);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {
		if (control instanceof PicklistEntryControl) {
			PicklistEntryControl ibcCon = (PicklistEntryControl) control;
			ibcCon.setCssClass(cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	public String getFieldMarkedCssClass(IControl control) {
		if (control instanceof PicklistEntryControl) {
			PicklistEntryControl ibcCon = (PicklistEntryControl) control;
			return ibcCon.getCssClass();
		}
		return null;
	}

}
