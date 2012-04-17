/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.TabStripControl;

/**
 * TabStrip with a better layout.
 * @author Florian Lippisch
 */
public class TabStripControlFix extends TabStripControl {

	/**
	 * @param container
	 * @param name
	 */
	public TabStripControlFix(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * @param container
	 */
	public TabStripControlFix(IControlContainer container) {
		super(container);
	}

}
