/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.security.IUser;

/**
 * Wrapper used in the editors JavaScript engine to access current user attributes
 * and check rights. This allows more simple hideWhen formulas that check
 * rights of a user using <code>user.hasRight(scope, action)</code>.
 * 
 * @author lippisch
 */
public class EditorContextUser {

	private ISecurityManager securityManager;
	private IUser currentUser;

	/**
	 * @param securityManager
	 */
	public EditorContextUser() {
		super();
		this.securityManager = DAOSystem.getSecurityManager();
		currentUser = securityManager.getCurrentUser();
	}

	/**
	 * @param scope
	 * @param action
	 * @return
	 * @see de.xwic.appkit.core.dao.ISecurityManager#hasRight(java.lang.String, java.lang.String)
	 */
	public boolean hasRight(String scope, String action) {
		return securityManager.hasRight(scope, action);
	}

	/**
	 * @param scope
	 * @param subscope
	 * @return
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getAccess(java.lang.String, java.lang.String)
	 */
	public int getAccess(String scope, String subscope) {
		return securityManager.getAccess(scope, subscope);
	}

	/**
	 * @return
	 * @see de.xwic.appkit.core.security.IUser#getLogonName()
	 */
	public String getLogonName() {
		return currentUser.getLogonName();
	}

	/**
	 * @return
	 * @see de.xwic.appkit.core.security.IUser#getName()
	 */
	public String getName() {
		return currentUser.getName();
	}

	/**
	 * @return
	 * @see de.xwic.appkit.core.security.IUser#getProfileName()
	 */
	public String getProfileName() {
		return currentUser.getProfileName();
	}

	/**
	 * @return
	 * @see de.xwic.appkit.core.security.IUser#getLanguage()
	 */
	public String getLanguage() {
		return currentUser.getLanguage();
	}
	
}
