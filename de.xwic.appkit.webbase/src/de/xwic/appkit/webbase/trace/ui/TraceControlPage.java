/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 * 
 */
package de.xwic.appkit.webbase.trace.ui;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
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

		TabStrip tabStrip = new TabStrip(this, "tabStrip");
		
		Tab tab = tabStrip.addTab("Options");
		new TraceOptionsControl(tab, null);
		
		tab = tabStrip.addTab("Statistic");
		new TraceStatView(tab, null);
		
		tab = tabStrip.addTab("Trace History");
		new TraceHistoryView(tab, null);
		
	}
}
