/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.base.JWicException;
import de.jwic.controls.Tab;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * A page on a a GenericEditor that contains editor content and a message area for warnings and hints. An EditorPage is typically
 * the only child control of a Tab element.
 * 
 * @author lippisch
 */
public class EditorContentPage extends ControlContainer {
	
	private EditorMessagesControl editorMessages;
	
	private String title = null;
	private Severity stateIndicator = null;

	private List<String> contentControlIds = new ArrayList<String>();
	
	/**
	 * @param container
	 * @param name
	 */
	public EditorContentPage(IControlContainer container, String name) {
		super(container, name);
		
		editorMessages = new EditorMessagesControl(this, "editorMessages");

	}
	

	/**
	 * Clear all warnings, errors and info messages. 
	 */
	public void resetMessages() {
		editorMessages.clear();
	}
	
	/**
	 * Add a message to be displayed as 'error'.
	 * @param message
	 */
	public void addError(String message) {
		editorMessages.addMessage(new EditorMessage(message, EditorMessage.Severity.ERROR));
	}

	/**
	 * Add a message to be displayed as 'warning'.
	 * @param message
	 */
	public void addWarn(String message) {
		editorMessages.addMessage(new EditorMessage(message, EditorMessage.Severity.WARNING));
	}
	
	/**
	 * Add a message to be displayed as 'info'.
	 * @param message
	 */
	public void addInfo(String message) {
		editorMessages.addMessage(new EditorMessage(message, EditorMessage.Severity.INFO));
	}

	/**
	 * Returns true if there are error messages.
	 * @return
	 */
	public boolean hasErrors() {
		return editorMessages.hasMessages(EditorMessage.Severity.ERROR);
	}

	/**
	 * Returns true if there are warning messages.
	 * @return
	 */
	public boolean hasWarnings() {
		return editorMessages.hasMessages(EditorMessage.Severity.WARNING);
	}

	/**
	 * Returns true if there are info messages.
	 * @return
	 */
	public boolean hasInfos() {
		return editorMessages.hasMessages(EditorMessage.Severity.INFO);
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
				+ ((editorMessages == null) ? 0 : editorMessages.hashCode());
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
		EditorContentPage other = (EditorContentPage) obj;
		if (contentControlIds == null) {
			if (other.contentControlIds != null)
				return false;
		} else if (!contentControlIds.equals(other.contentControlIds))
			return false;
		if (editorMessages == null) {
			if (other.editorMessages != null)
				return false;
		} else if (!editorMessages.equals(other.editorMessages))
			return false;
		return true;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the stateIndicator
	 */
	public Severity getStateIndicator() {
		return stateIndicator;
	}


	/**
	 * @param stateIndicator the stateIndicator to set
	 */
	public void setStateIndicator(Severity stateIndicator) {
		this.stateIndicator = stateIndicator;
		
		// the hack.... - this should go into an event based model where the EditorPage throws a propertyChanged event and the editor updates the
		// respective tab... but for now.. for the demo... 
		
		if (getContainer() instanceof Tab) {
			
			Tab myTab = (Tab)getContainer();
			ImageRef imgIndicator = null;
			if (stateIndicator != null) {
				switch (stateIndicator) {
				case ERROR:
					imgIndicator = ImageLibrary.ICON_ERROR;
					break;
				case WARN:
					imgIndicator = ImageLibrary.ICON_WARNING;
					break;
				}
			}
			String newTitle;
			if (imgIndicator != null) {
				newTitle = title + " " + imgIndicator.toImgTag();
			} else {
				newTitle = title;
			}
			if (!newTitle.equals(myTab.getTitle())) {
				myTab.setTitle(newTitle);
				((ControlContainer)myTab.getContainer()).requireRedraw();
			}
			
		}
		
	}
	
}
