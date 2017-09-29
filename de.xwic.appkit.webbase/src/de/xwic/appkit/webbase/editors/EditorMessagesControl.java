/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;

/**
 * Displays a list of messages in an error, warning or info state. Messages are grouped by severity.
 * 
 * @author lippisch
 */
public class EditorMessagesControl extends Control {
	
	private List<EditorMessage> messages = new ArrayList<EditorMessage>();
	
	/**
	 * @param container
	 * @param name
	 */
	public EditorMessagesControl(IControlContainer container, String name) {
		super(container, name);
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
	 * Returns true if the control has messages of the specified severity.
	 * @param severity
	 * @return
	 */
	public boolean hasMessages(EditorMessage.Severity severity) {
		for (EditorMessage msg : messages) {
			if (msg.getSeverity() == severity) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a message to the list of messages, causing a re-draw of the control.
	 * @param message
	 */
	public void addMessage(String message) {
		messages.add(new EditorMessage(message));
		requireRedraw();
	}

	/**
	 * Add a message.
	 * @param msg
	 */
	public void addMessage(EditorMessage msg) {
		messages.add(msg);
		requireRedraw();
	}
	
	/**
	 * @return the messages
	 */
	public List<EditorMessage> getMessages() {
		return messages;
	}

	/**
	 * @param staticMessages
	 */
	public void setMessages(List<EditorMessage> messages) {
		this.messages = messages;
		requireRedraw();
	}
	
}
