/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.Set;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryMultiSelectControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public class ToolkitPicklistSelectionMultiControl extends AbstractToolkitHTMLElementControl<PicklistEntryMultiSelectControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public PicklistEntryMultiSelectControl create(IControlContainer container, String name, Object optionalParam) {
		return new PicklistEntryMultiSelectControl(container, name, (String) optionalParam);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(PicklistEntryMultiSelectControl control) {
		return control.getSelectedEntries();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(PicklistEntryMultiSelectControl control, Object obj) {
		control.selectEntries((Set<IPicklistEntry>) obj);
	}
}
