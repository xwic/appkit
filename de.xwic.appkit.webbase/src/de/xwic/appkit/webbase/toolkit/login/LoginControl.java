/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.netapp.spc.toolkit.login.LoginControl
 * Created on Aug 20, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.toolkit.login;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.ButtonControl;
import de.jwic.controls.InputBoxControl;
import de.jwic.controls.LabelControl;
import de.jwic.ecolib.controls.ErrorWarningControl;
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

	private InputBoxControl inpUsername;
	private InputBoxControl inpPassword;
	private ErrorWarningControl errorInfo;

	private LoginModel model;

	/**
	 * @param container
	 * @param name
	 */
	public LoginControl(IControlContainer container, String name, LoginModel model) {
		super(container, name);

		this.model = model;

		inpUsername = new InputBoxControl(this, "inpUsername");
		inpUsername.setText("");
		//inpUsername.setFillWidth(true);
		
		inpUsername.forceFocus();

		
		inpPassword = new InputBoxControl(this, "inpPassword");
		inpPassword.setPassword(true);
		//inpPassword.setFillWidth(true);
		inpPassword.setListenKeyCode(13); // listen to ENTER key
		inpPassword.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				doLogin(); // call logon on ENTER key
			}
		});

		errorInfo = new ErrorWarningControl(this, "errorInfo");

		Bundle bundle = BundleAccessor.getDomainBundle(this, ExtendedApplication.CORE_DOMAIN_ID);
		
		ButtonControl btSubmit = new ButtonControl(this, "btSubmit");
		btSubmit.setTitle(bundle.getString("login.btn.title"));
		btSubmit.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				doLogin();
			}
		});

		LabelControl loginTitle = new LabelControl(this, "title");
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
