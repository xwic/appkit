/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author Adrian Ionescu
 */
public class ToolkitSimpleCheckBoxControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public IControl create(IControlContainer container, String name, Object optionalParam) {
		CheckBox control = new CheckBox(container, name);
		String title = optionalParam == null ? "" : (String)optionalParam;
		control.setLabel(title);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(IControl control) {
		if (control instanceof CheckBox) {
			CheckBox ctrl = (CheckBox) control;
			return ctrl.isChecked();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(IControl control, Object obj) {
		if (control instanceof CheckBox) {
			CheckBox ctrl = (CheckBox) control;
			ctrl.setChecked(obj != null ? (Boolean) obj : false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(IControl control, String cssClass) {
		if (control instanceof CheckBox) {
			CheckBox ctrl = (CheckBox) control;
			ctrl.setCssClass(cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(IControl control) {
		if (control instanceof CheckBox) {
			CheckBox ctrls = (CheckBox) control;
			return ctrls.getCssClass();
		}
		return null;
	}

}
