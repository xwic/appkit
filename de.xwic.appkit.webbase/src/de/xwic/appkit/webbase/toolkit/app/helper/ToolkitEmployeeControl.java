/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.components.EmployeeSelectionCombo;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitEmployeeControl implements IToolkitControlHelper {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	public IControl create(IControlContainer container, String name, Object optionalParam) {
		boolean allowEmpty = false;
		if (optionalParam instanceof Boolean) {
			allowEmpty = ((Boolean)optionalParam).booleanValue();
		}
		EmployeeSelectionCombo control = new EmployeeSelectionCombo(container, name, allowEmpty);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	public Object getContent(IControl control) {
		if (control instanceof EmployeeSelectionCombo) {
			EmployeeSelectionCombo ibcCon = (EmployeeSelectionCombo) control;
			return ibcCon.getSelectedEntry();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	public void loadContent(IControl control, Object obj) {
		if (control instanceof EmployeeSelectionCombo) {
			EmployeeSelectionCombo ibcCon = (EmployeeSelectionCombo) control;
			ibcCon.selectEntry((IEntity) obj);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	public void markField(IControl control, String cssClass) {
		if (control instanceof InputBox) {
			EmployeeSelectionCombo ibcCon = (EmployeeSelectionCombo) control;
			ibcCon.setCssClass(cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	public String getFieldMarkedCssClass(IControl control) {
		if (control instanceof EmployeeSelectionCombo) {
			EmployeeSelectionCombo ibcCon = (EmployeeSelectionCombo) control;
			return ibcCon.getCssClass();
		}
		return null;
	}

}
