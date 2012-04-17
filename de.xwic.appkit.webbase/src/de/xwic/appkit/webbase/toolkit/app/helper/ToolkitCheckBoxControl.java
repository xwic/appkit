/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckboxControl;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitCheckBoxControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	public IControl create(IControlContainer container, String name, Object optionalParam) {
		CheckboxControl control = new CheckboxControl(container, name);
		String title = optionalParam == null ? "" : (String)optionalParam;
		control.addElement(title, name);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		if (control instanceof CheckboxControl) {
			CheckboxControl ibcCon = (CheckboxControl) control;
			return ibcCon.isKeySelected(ibcCon.getName());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	public void loadContent(IControl control, Object obj) {
		if (control instanceof CheckboxControl) {
			CheckboxControl ibcCon = (CheckboxControl) control;
			if ((Boolean) obj) {
				ibcCon.setSelectedKey(ibcCon.getName());
			} else {
				ibcCon.setSelectedKey("");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {
		if (control instanceof CheckboxControl) {
			CheckboxControl ibcCon = (CheckboxControl) control;
			ibcCon.setCssClass(cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	public String getFieldMarkedCssClass(IControl control) {
		if (control instanceof CheckboxControl) {
			CheckboxControl ibcCon = (CheckboxControl) control;
			return ibcCon.getCssClass();
		}
		return null;
	}

}
