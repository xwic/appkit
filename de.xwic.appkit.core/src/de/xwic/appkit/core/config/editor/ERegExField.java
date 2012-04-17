/**
 * 
 */
package de.xwic.appkit.core.config.editor;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ERegExField extends EField {
	
	private String regExPattern = null;

	
	/**
	 * @return the regExPattern
	 */
	public String getRegExPattern() {
		return regExPattern;
	}

	/**
	 * @param regExPattern the regExPattern to set
	 */
	public void setRegExPattern(String regExPattern) {
		this.regExPattern = regExPattern;
	}

}
