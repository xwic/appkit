/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

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
