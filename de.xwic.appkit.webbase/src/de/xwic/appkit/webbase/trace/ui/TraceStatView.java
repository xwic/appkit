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

package de.xwic.appkit.webbase.trace.ui;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;

/**
 * Displays statistics for the trace history.
 * @author lippisch
 */
public class TraceStatView extends ControlContainer {

	private TraceStatModel model;

	/**
	 * @param container
	 * @param name
	 */
	public TraceStatView(IControlContainer container, String name) {
		super(container, name);
		
		Button btGenerate = new Button(this, "btGenerate");
		btGenerate.setTitle("Generate Statistic");
		btGenerate.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onGenerateStats();
			}
		});
		
		model = new TraceStatModel();
		
		new TraceStatGraph(this, "traceGraphAvg", model, TraceStatGraph.Type.Average);
		new TraceStatGraph(this, "traceGraphCnt", model, TraceStatGraph.Type.Count);
		new TraceStatGraph(this, "traceGraphPeak", model, TraceStatGraph.Type.Peak);
		new TraceStatGraph(this, "traceGraphTotal", model, TraceStatGraph.Type.Total);
		
	}

	/**
	 * Generate the statistics.
	 */
	protected void onGenerateStats() {
		
		model.refresh();
		
	}

}
