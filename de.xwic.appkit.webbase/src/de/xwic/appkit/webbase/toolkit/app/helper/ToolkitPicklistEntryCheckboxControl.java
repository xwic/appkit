/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl;

/**
 * @author dotto
 *
 */
public class ToolkitPicklistEntryCheckboxControl implements IToolkitControlHelper<PicklistEntryCheckboxControl> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public PicklistEntryCheckboxControl create(IControlContainer container, String name, Object optionalParam) {
		return new PicklistEntryCheckboxControl(container, name, (String) optionalParam);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(PicklistEntryCheckboxControl control) {
		List<IPicklistEntry> list = control.getSelectedEntries();
		if(list == null){
			return null;
		}
		return new HashSet<IPicklistEntry>(list);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(PicklistEntryCheckboxControl control, Object obj) {
		Set<IPicklistEntry> set = (Set<IPicklistEntry>) obj;
		ArrayList<IPicklistEntry> result = new ArrayList<IPicklistEntry>();
		if(set != null){
			for (IPicklistEntry pe : set) {
				result.add(pe);
			}
		}
		control.selectEntries(result);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(PicklistEntryCheckboxControl control, String cssClass) {
		control.setCssClass(cssClass);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(PicklistEntryCheckboxControl control) {
		return control.getCssClass();
	}
}
