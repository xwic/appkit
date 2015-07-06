/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
