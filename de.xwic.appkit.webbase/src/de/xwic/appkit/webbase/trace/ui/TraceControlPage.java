/**
 * 
 */
package de.xwic.appkit.webbase.trace.ui;

import de.jwic.base.IControlContainer;
import de.jwic.controls.TabControl;
import de.jwic.controls.TabStripControl;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;

/**
 * Control the Trace facility and view trace statistics.
 * 
 * @author Florian Lippisch
 */
public class TraceControlPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public TraceControlPage(IControlContainer container, String name) {
		super(container, name);
		
		setTitle("Trace Control");
		setSubtitle("Manage the trace functionality and view statistics.");

		TabStripControl tabStrip = new TabStripControl(this, "tabStrip");
		
		TabControl tab = tabStrip.addTab("Options");
		new TraceOptionsControl(tab, null);
		
		tab = tabStrip.addTab("Statistic");
		new TraceStatView(tab, null);
		
		tab = tabStrip.addTab("Trace History");
		new TraceHistoryView(tab, null);
		
	}
}
