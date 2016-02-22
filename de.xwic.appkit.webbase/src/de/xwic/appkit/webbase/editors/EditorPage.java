/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.JWicException;

/**
 * A page on a a GenericEditor that contains editor content and a message area for warnings and hints. An EditorPage is typically
 * the only child control of a Tab element.
 * 
 * @author lippisch
 */
public class EditorPage extends ControlContainer {
	
	private EditorMessagesControl msgError;
	private EditorMessagesControl msgWarn;
	private EditorMessagesControl msgInfo;

	private List<String> contentControlIds = new ArrayList<String>();
	
	/**
	 * @param container
	 * @param name
	 */
	public EditorPage(IControlContainer container, String name) {
		super(container, name);
		
		msgError = new EditorMessagesControl(this, "msgError");
		msgError.setStyleClass(EditorMessagesControl.CLASS_ERROR);

		msgWarn = new EditorMessagesControl(this, "msgWarn");
		msgWarn.setStyleClass(EditorMessagesControl.CLASS_WARN);

		msgInfo = new EditorMessagesControl(this, "msgInfo");
		msgInfo.setStyleClass(EditorMessagesControl.CLASS_INFO);

	}
	

	/**
	 * Clear all warnings, errors and info messages. 
	 */
	public void resetMessages() {
		msgError.clear();
		msgWarn.clear();
		msgInfo.clear();
	}
	
	/**
	 * Add a message to be displayed as 'error'.
	 * @param message
	 */
	public void addError(String message) {
		msgError.addMessage(message);
	}

	/**
	 * Add a message to be displayed as 'warning'.
	 * @param message
	 */
	public void addWarn(String message) {
		msgWarn.addMessage(message);
	}
	
	/**
	 * Add a message to be displayed as 'info'.
	 * @param message
	 */
	public void addInfo(String message) {
		msgInfo.addMessage(message);
	}

	/**
	 * Returns true if there are error messages.
	 * @return
	 */
	public boolean hasErrors() {
		return !msgError.getMessages().isEmpty();
	}

	/**
	 * Returns true if there are warning messages.
	 * @return
	 */
	public boolean hasWarnings() {
		return !msgWarn.getMessages().isEmpty();
	}

	/**
	 * Returns true if there are info messages.
	 * @return
	 */
	public boolean hasInfos() {
		return !msgInfo.getMessages().isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#registerControl(de.jwic.base.Control, java.lang.String)
	 */
	@Override
	public void registerControl(Control control, String name) throws JWicException {
		super.registerControl(control, name);
		
		// keep track of child controls that get added that are not our own, so that they can be rendered in sequence as 
		// they got added
		if (!(control instanceof EditorMessagesControl)) {
			contentControlIds.add(control.getName());
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#removeControl(java.lang.String)
	 */
	@Override
	public void removeControl(String controlName) {
		super.removeControl(controlName);
		contentControlIds.remove(controlName);
	}

	/**
	 * @return the contentControlIds
	 */
	public List<String> getContentControlIds() {
		return contentControlIds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((contentControlIds == null) ? 0 : contentControlIds
						.hashCode());
		result = prime * result
				+ ((msgError == null) ? 0 : msgError.hashCode());
		result = prime * result + ((msgInfo == null) ? 0 : msgInfo.hashCode());
		result = prime * result + ((msgWarn == null) ? 0 : msgWarn.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorPage other = (EditorPage) obj;
		if (contentControlIds == null) {
			if (other.contentControlIds != null)
				return false;
		} else if (!contentControlIds.equals(other.contentControlIds))
			return false;
		if (msgError == null) {
			if (other.msgError != null)
				return false;
		} else if (!msgError.equals(other.msgError))
			return false;
		if (msgInfo == null) {
			if (other.msgInfo != null)
				return false;
		} else if (!msgInfo.equals(other.msgInfo))
			return false;
		if (msgWarn == null) {
			if (other.msgWarn != null)
				return false;
		} else if (!msgWarn.equals(other.msgWarn))
			return false;
		return true;
	}
	
}
