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
