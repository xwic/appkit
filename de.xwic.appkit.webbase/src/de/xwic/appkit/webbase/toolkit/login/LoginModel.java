/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.netapp.spc.toolkit.login.LoginModel
 * Created on Aug 20, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.toolkit.login;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.dao.DAOSystem;



/**
 * Login model. Handles the call to the SecurityManager to login the user to the application.
 * @author Aron Cotrau
 */
public class LoginModel {

	private static final int LOGIN_SUCCESS = 1;
	
	private List<ILoginListener> listeners = new ArrayList<ILoginListener>();
	
	/**
	 * the Login listener
	 * @author Aron Cotrau
	 */
	public interface ILoginListener {
		
		/**
		 * called when login was successful
		 */
		public void loginSuccessful();
		
	}
	
	/**
	 * notifies the listeners for the given event type
	 * @param eventType
	 */
	private void notifyEvent(int eventType) {
		switch (eventType) {
		case LOGIN_SUCCESS:
			notifyLogin();
			break;

		default:
			break;
		}
	}
	
	/**
	 * @param userName
	 * @param passWord
	 */
	public void login(String userName, String password) throws Exception {

		if (DAOSystem.getSecurityManager().logon(userName, password)) {
			notifyEvent(LOGIN_SUCCESS);
		} else {
			throw new SecurityException("Invalid Username/Password");
		}
		
	}
	
	private void notifyLogin() {
		for (ILoginListener listener : listeners) {
			listener.loginSuccessful();
		}
	}

	/**
	 * adds the given listener to the listeners list
	 * @param listener
	 */
	public synchronized void addListener(ILoginListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * removes the given listener from the list
	 * @param listener
	 */
	public synchronized void removeListener(ILoginListener listener) {
		listeners.remove(listener);
	}

}
