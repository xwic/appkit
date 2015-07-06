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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.events.KeyEvent;
import de.jwic.events.KeyListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;

/**
 * Defines the login control defined for the SPC login procedure.
 * 
 * @author Aron Cotrau
 */
public class LoginControl extends ControlContainer {

	private InputBox inpUsername;
	private InputBox inpPassword;
	private ErrorWarning errorInfo;

	private LoginModel model;

	/**
	 * @param container
	 * @param name
	 */
	public LoginControl(IControlContainer container, String name, LoginModel model) {
		super(container, name);

		this.model = model;

		inpUsername = new InputBox(this, "inpUsername");
		inpUsername.setText("");
		//inpUsername.setFillWidth(true);
		
		inpUsername.forceFocus();

		
		inpPassword = new InputBox(this, "inpPassword");
		inpPassword.setPassword(true);
		//inpPassword.setFillWidth(true);
		inpPassword.setListenKeyCode(13); // listen to ENTER key
		inpPassword.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				doLogin(); // call logon on ENTER key
			}
		});

		errorInfo = new ErrorWarning(this, "errorInfo");

		Bundle bundle = BundleAccessor.getDomainBundle(this, ExtendedApplication.CORE_DOMAIN_ID);
		
		Button btSubmit = new Button(this, "btSubmit");
		btSubmit.setTitle(bundle.getString("login.btn.title"));
		btSubmit.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				doLogin();
			}
		});

		Label loginTitle = new Label(this, "title");
		loginTitle.setText(bundle.getString("login.title"));
	}

	/**
	 * handles the call to the model to the login method
	 */
	protected void doLogin() {

		try {
			
			model.login(inpUsername.getText(), inpPassword.getText());
			
		} catch (Exception e) {
			errorInfo.showError("Authentication failed (" + e.getMessage() + ")");
			inpPassword.forceFocus();
		}
		
		inpPassword.setText("");

	}
}
