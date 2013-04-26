/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.IControlContainer;
import de.jwic.controls.AnchorLink;
import de.xwic.appkit.core.dao.DAOSystem;

/**
 * Control for the exit button.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class LogoutControl extends AnchorLink {
    
	private String confirmMessage;

	/**
	 * Creates the control.
	 * 
	 * @param container
	 */
    public LogoutControl(IControlContainer container) {
    	super(container);   
    }

    /**
     * Creates the control.
     * 
     * @param container
     * @param name
     */
    public LogoutControl(IControlContainer container, String name) { 
    	super(container, name);

    }

    
    /**
     * the confirm message.
     * 
     * @return
     */
    public String getConfirmMessage() {
		return confirmMessage;
	}

    /**
     * sets the confirm message.
     * 
     * @param confirmMessage
     */
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	/*
	 * (non-Javadoc)
	 * @see de.jwic.controls.AnchorLinkControl#actionPerformed(java.lang.String, java.lang.String)
	 */
	public void actionPerformed(String actionId, String parameter) {
		DAOSystem.getSecurityManager().logout();
        getSessionContext().exit();
    }
}
