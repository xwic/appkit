/**
 * 
 */
package de.xwic.appkit.webbase.dialog;

import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public abstract class AbstractPopUpDialogWindow extends AbstractDialogWindow {

	/**
	 * @param site
	 */
	public AbstractPopUpDialogWindow(Site site) {
		super(site);
		
		setTemplateName(AbstractPopUpDialogWindow.class.getName());
		setCssClass("j-combo-content");
		setModal(false);
	}

}
