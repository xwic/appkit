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

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONWriter;

import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.json.JsonResourceControl;
import de.jwic.util.SerObservable;
import de.jwic.util.SerObserver;
import de.xwic.cube.ICube;
import de.xwic.cube.IDimensionElement;
import de.xwic.cube.IMeasure;
import de.xwic.cube.Key;

/**
 * Renders the graph of the trace using plain JavaScript on the client side.
 * 
 * @author lippisch
 */
@JavaScriptSupport
public class TraceStatGraph extends JsonResourceControl {

	public enum Type {
		Average,
		Peak,
		Count,
		Total		
	}
	
	private final TraceStatModel model;
	private final Type type;

	/**
	 * @param container
	 * @param name
	 * @param model 
	 */
	public TraceStatGraph(IControlContainer container, String name, TraceStatModel model, Type type) {
		super(container, name);
		this.model = model;
		this.type = type;
		
		model.addObserver(new SerObserver() {
			@Override
			public void update(SerObservable o, Object arg) {
				requireRedraw();
			}
		});
		
	}

	/**
	 * Returns true if the model has data to display.
	 * @return
	 */
	public boolean isHasData() {
		return model.getCube() != null;
	}

	public String getTypeName() {
		return type.toString();
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.json.JsonResourceControl#handleJSONResponse(javax.servlet.http.HttpServletRequest, org.json.JSONWriter)
	 */
	@Override
	public void handleJSONResponse(HttpServletRequest req, JSONWriter out) throws JSONException {
		
		ICube cube = model.getCube();
		if (cube != null) {
			
			IMeasure meDuration = model.getMeasureDuration();
			IMeasure meCount = model.getMeasureCount();
			IMeasure mePeak = model.getMeasurePeak();

			double maxScale = 0;
			String measureTitle = "ms";
			switch (type) {
			case Average:
				maxScale = model.getHighestAvr();
				break;
			case Peak:
				maxScale = model.getHighestPeak();
				break;
			case Count:
				maxScale = model.getHighestCount();
				measureTitle = "";
				break;
			case Total:
				maxScale = model.getHighestDuration();
				break;
			}
			
			out.object();
			out.key("maxScale");
			out.value(maxScale);

			out.key("measureTitle");
			out.value(measureTitle);
			
			out.key("data");
			out.array();
			
			// loop through data
			for (IDimensionElement eTime : model.getDimensionTime().getDimensionElements()) {
				out.object();
				out.key("title");
				out.value(eTime.getKey());
				out.key("value");
				
				Double value = null;
				
				Key cKey = cube.createKey(eTime);

				switch (type) {
				case Average: {
					Double valDur = cube.getCellValue(cKey, meDuration);
					Double valCnt = cube.getCellValue(cKey, meCount);
					if (valDur != null && valCnt != null) {
						double avr = valDur / valCnt;
						value = avr;
					}
					}
					break;
				case Peak: 
					value = cube.getCellValue(cKey, mePeak);
					break;
				case Count:
					value = cube.getCellValue(cKey, meCount);
					break;
				case Total:
					value = cube.getCellValue(cKey, meDuration);
					break;
				}

				out.value(value != null ? value : 0);
				
				out.endObject();
			}
			
			out.endArray();
			out.endObject();
			
		} else {
			out.object();
			out.key("nodata");
			out.value("nodata");
			out.endObject();
		}
		
	}

}
