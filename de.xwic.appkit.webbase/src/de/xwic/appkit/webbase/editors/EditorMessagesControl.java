/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;

/**
 * Displays a list of messages in an error, warning or info state. An EditorMessagesControl can only 
 * display messages in a single style.
 * 
 * @author lippisch
 */
public class EditorMessagesControl extends Control {

	public final static String CLASS_ERROR = "xwic-ed-msg-error";
	public final static String CLASS_WARN = "xwic-ed-msg-warn";
	public final static String CLASS_INFO = "xwic-ed-msg-info";

	private String styleClass = CLASS_INFO;
	
	private List<String> messages = new ArrayList<String>();
	
	/**
	 * @param container
	 * @param name
	 */
	public EditorMessagesControl(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * Remove all messages from the list.
	 */
	public void clear() {
		if (messages.size() > 0) {
			messages.clear();
			requireRedraw();
		}
	}
	
	/**
	 * Add a message to the list of messages, causing a re-draw of the control.
	 * @param message
	 */
	public void addMessage(String message) {
		messages.add(message);
		requireRedraw();
	}
	
	
	/**
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
}
