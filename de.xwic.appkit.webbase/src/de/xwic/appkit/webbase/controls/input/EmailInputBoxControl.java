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

import de.jwic.base.IControlContainer;

/**
 * Meant to check the input and roughly validate it using a simple regex.
 * 
 * Parsing is handled by {@link EmailInputType}
 *
 * @author Martin Weinand
 */
public class EmailInputBoxControl extends TypedInputBoxControl<EmailInputType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8405542687887703810L;

	/**
	 * @param container
	 * @param name
	 */
	public EmailInputBoxControl(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * @param container
	 */
	public EmailInputBoxControl(IControlContainer container) {
		super(container);
	}

	/**
	 * String with potentially multiple email addresses, delimited by ','.
	 * No whitespaces.
	 * 
	 * @return
	 */
	public String getEmails(){
		EmailInputType typedValue = getTypedValue();
		if(typedValue != null){
			return typedValue.getString();
		}
		else{
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.controls.input.TypedInputBoxControl#parseTypedValue()
	 */
	@Override
	protected EmailInputType parseTypedValue() throws TypedInputBoxControlParseException {
		EmailInputType returnValue;
		
		String text = getText();
		if(text == null || text.isEmpty()){
			returnValue = null;
		}
		else{
			EmailInputType emailInputType = new EmailInputType(text);
			if(emailInputType.isValid()){
				returnValue = emailInputType;
			}
			else{
				returnValue = null;
				throw new TypedInputBoxControlParseException();
			}
		}
		
		return returnValue;
	}
}
