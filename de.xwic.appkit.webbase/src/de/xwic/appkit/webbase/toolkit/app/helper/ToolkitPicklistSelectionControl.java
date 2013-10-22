/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitPicklistSelectionControl extends AbstractToolkitListBoxControl<IPicklistEntry, PicklistEntryControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	@Override
	public PicklistEntryControl create(IControlContainer container, String name, Object optionalParam) {
		return new PicklistEntryControl(container, name, (String) optionalParam);
	}

}
