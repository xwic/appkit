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
package de.xwic.appkit.webbase.controls.input;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.jwic.base.IControlContainer;

/**
 *	Default NumberFormat is the US number format returned by <code>NumberFormat.getInstance(Locale.US)</code>
 *
 * @author Martin Weinand
 */
public class DoubleInputBoxControl extends TypedInputBoxControl<Double>{

	/**
	 * css class should set to right aligned since we are accessing/displaying numbers
	 */
	public static final String CSS_CLASS = "de_xwic_appkit_webbase_controls_input_doubleInputBoxControl";
	
	private NumberFormat format;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6820881610200274599L;

	/**
	 * @param container
	 * @param name
	 */
	public DoubleInputBoxControl(IControlContainer container, String name, NumberFormat format) {
		super(container, name);
		init(format);
	}

	/**
	 * @param container
	 */
	public DoubleInputBoxControl(IControlContainer container, NumberFormat format) {
		super(container);
		init(format);
	}
	
	/**
	 * @param container
	 * @param name
	 */
	public DoubleInputBoxControl(IControlContainer container, String name) {
		super(container, name);
		init(null);
	}

	/**
	 * @param container
	 */
	public DoubleInputBoxControl(IControlContainer container) {
		super(container);
		init(null);
	}
	
	private void init(NumberFormat format2){
		
		setCssClass(getCssClass() + " " +CSS_CLASS);
		
		if(format2 == null){
			format2 = NumberFormat.getInstance(Locale.US);
		}
		
		this.format = format2;
		
	}
	
	/**
	 * More direct version of {@link #getTypedValue()}
	 * @return
	 */
	public Double getDouble(){
		return getTypedValue();
	}
	
	/**
	 * Sets this field's text to a string representation of d.
	 * 
	 * @param d the Double to set.
	 */
	public void setDouble(Double d){
		if(d == null){
			setText(null);
		}
		else{
			setText(format.format(d));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.controls.input.TypedInputBoxControl#parseTypedValue()
	 */
	@Override
	protected Double parseTypedValue() throws TypedInputBoxControlParseException {
		Double returnValue;
		
		String rawValue = getText();
		
		if(rawValue == null || rawValue.isEmpty()){
			returnValue = null;
		}
		else{
			
			try {
				return format.parse(rawValue).doubleValue();
			} catch (ParseException e2) {
				returnValue = null;
				throw new TypedInputBoxControlParseException();
			}
			
		}
		
		return returnValue;
	}
	
}
