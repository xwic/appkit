/*
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
**/
package de.xwic.appkit.webbase.editors;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import de.jwic.base.IControl;

/**
 * Wrapps a widget with it's hideWhen formula to be evaluated.
 * 
 * @author Lippisch
 */
public class HideWhenWidgetWrapper {

	private IControl widget;
	private String hideWhenFormula;

	/**
	 * 
	 */
	public HideWhenWidgetWrapper(IControl widget, String hideWhenFormula) {
		this.widget = widget;
		this.hideWhenFormula = hideWhenFormula;
		
	}
	
	/**
	 * Evaluate the hideWhen formula and toggle the visible state of the 
	 * widget accordingly.
	 * 
	 * @param engine
	 * @throws ScriptException
	 */
	public void evaluate(ScriptEngine engine) throws ScriptException {
		
		Object result = engine.eval(hideWhenFormula);
		boolean hide = false;
		if (result instanceof Boolean) {
			hide = ((Boolean)result).booleanValue();
		}
		
		widget.setVisible(!hide);
		
	}
	
}
