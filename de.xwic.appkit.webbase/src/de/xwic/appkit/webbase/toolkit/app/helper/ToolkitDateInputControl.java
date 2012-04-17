/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.Date;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.DateInputBoxControl;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitDateInputControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	public IControl create(IControlContainer container, String name, Object optionalParam) {
		DateInputBoxControl control = new DateInputBoxControl(container, name);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		if (control instanceof DateInputBoxControl) {
			DateInputBoxControl ibcCon = (DateInputBoxControl) control;
			return ibcCon.getDate();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	public void loadContent(IControl control, Object obj) {
		if (control instanceof DateInputBoxControl) {
			DateInputBoxControl ibcCon = (DateInputBoxControl) control;
			ibcCon.setDate((Date)obj);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {
		if (control instanceof DateInputBoxControl) {
			DateInputBoxControl ibcCon = (DateInputBoxControl) control;
			ibcCon.setCssClass(cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	public String getFieldMarkedCssClass(IControl control) {
		if (control instanceof DateInputBoxControl) {
			DateInputBoxControl ibcCon = (DateInputBoxControl) control;
			return ibcCon.getCssClass();
		}
		return null;
	}
}
