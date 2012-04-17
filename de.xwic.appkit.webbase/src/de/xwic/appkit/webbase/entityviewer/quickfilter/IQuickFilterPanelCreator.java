/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.quickfilter;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public interface IQuickFilterPanelCreator {

	/**
	 * @param container
	 * @param name
	 * @param tableModel
	 * @return
	 */
	public AbstractQuickFilterPanel createQuickFilterPanel(IControlContainer container, EntityTableModel tableModel);
}
