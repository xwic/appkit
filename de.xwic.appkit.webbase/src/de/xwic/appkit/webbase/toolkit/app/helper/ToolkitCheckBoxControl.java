/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBoxGroup;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitCheckBoxControl implements IToolkitControlHelper<CheckBoxGroup> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public CheckBoxGroup create(IControlContainer container, String name, Object optionalParam) {
		CheckBoxGroup control = new CheckBoxGroup(container, name);
		String title = optionalParam == null ? "" : (String)optionalParam;
		control.addElement(title, name);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(CheckBoxGroup control) {
		return control.isKeySelected(control.getName());
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(CheckBoxGroup control, Object obj) {
		control.setSelectedKey(obj != null && (Boolean) obj ? control.getName() : "");
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(CheckBoxGroup control, String cssClass) {
		control.setCssClass(cssClass);
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(CheckBoxGroup control) {
		return control.getCssClass();
	}

}
