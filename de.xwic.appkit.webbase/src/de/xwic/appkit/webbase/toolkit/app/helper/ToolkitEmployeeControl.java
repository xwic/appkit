/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.toolkit.components.EmployeeSelectionCombo;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitEmployeeControl extends AbstractToolkitListBoxControl<IMitarbeiter, EmployeeSelectionCombo> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public EmployeeSelectionCombo create(IControlContainer container, String name, Object optionalParam) {
		boolean allowEmpty = false;
		if (optionalParam instanceof Boolean) {
			allowEmpty = ((Boolean)optionalParam).booleanValue();
		}
		return new EmployeeSelectionCombo(container, name, allowEmpty);
	}

}
