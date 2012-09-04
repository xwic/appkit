/*
 * Copyright 2005-2007 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.controls.input.TypedInputBoxControl
 * Created on 03.09.2012
 * $Id:$
 */
package de.xwic.appkit.webbase.controls.input;

import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBoxControl;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;

/**
 * An input box control that returns a typed value
 * @author Martin Weinand
 */
public abstract class TypedInputBoxControl<T> extends InputBoxControl{
	
	private static final long serialVersionUID = -3523158047520993885L;
	
	/**
	 * @param container
	 * @param name
	 */
	public TypedInputBoxControl(IControlContainer container, String name) {
		super(container, name);
		
		init();
	}

	/**
	 * @param container
	 */
	public TypedInputBoxControl(IControlContainer container) {
		super(container);
		
		init();
	}
	
	private void init(){
		this.setTemplateName(InputBoxControl.class.getName());
		
		this.addValueChangedListener(new ValueChangedListener() {
			private static final long serialVersionUID = -6528603430817190321L;

			@Override
			public void valueChanged(ValueChangedEvent event) {
				setFlagAsError(!isValid());
			}
		});
	}

	/**
	 * true if empty (parsed value is null) or the text value could be successfully parsed into a typed value
	 * @return
	 */
	public boolean isValid(){
		try {
			parseTypedValue();
		} catch (TypedInputBoxControlParseException t) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return <code>null</code> or the typed value
	 */
	public T getTypedValue(){
		
		try {
			return parseTypedValue();
		} catch (TypedInputBoxControlParseException e) {
			//ok at this point, we are not validating here
			return null;
		}
	}
	
	/**
	 * is called multiple times.
	 * 
	 * On each value change
	 * On each call of {@link #isValid()}
	 * On each call of {@link #getTypedValue()}
	 * 
	 * So if this method is costly, consider not using this base class
	 * 
	 * @return
	 */
	protected abstract T parseTypedValue() throws TypedInputBoxControlParseException;
}
