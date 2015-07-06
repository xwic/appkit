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
package de.xwic.appkit.core.config.editor;

/**
 * Special text efield for phone numbers. <p>
 * 
 * @author Ronny Pfretzschner
 * @editortag phone
 */
public class EPhoneText extends EField {

	private boolean validate = false;
	
	/**
	 * Create a new EField for the phone text field.
	 */
	public EPhoneText() {
		
	}

	/**
	 * @return true, if this field has to be validate for the update
	 */
	public boolean isValidate() {
		return validate;
	}

	/**
	 * This parameter is used to know whether or not this field should validate it's input.
	 * @param validate the validation flag to set
	 * @default false
	 */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
}
