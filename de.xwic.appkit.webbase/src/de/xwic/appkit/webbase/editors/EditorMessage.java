/**
 * 
 */
package de.xwic.appkit.webbase.editors;

/**
 * A message that is to be displayed by the editor.
 * @author lippisch
 */
public class EditorMessage {

	public enum Severity {
		INFO,
		WARNING,
		ERROR
	}
	
	private String id = null;
	private String message = null;
	private Severity severity = Severity.INFO;
	
	
	/**
	 * @param id
	 * @param message
	 * @param severity
	 */
	public EditorMessage(String message) {
		super();
		this.id = message;
		this.message = message;
		this.severity = Severity.INFO;
	}

	/**
	 * @param id
	 * @param message
	 * @param severity
	 */
	public EditorMessage(String message, Severity severity) {
		super();
		this.id = message;
		this.message = message;
		this.severity = severity;
	}
	
	/**
	 * @param message
	 * @param severity
	 * @param id
	 */
	public EditorMessage(String message, Severity severity, String id) {
		super();
		this.message = message;
		this.severity = severity;
		this.id = id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	
	
}
