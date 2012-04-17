/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.dialog;

/**
 * @author lippisch
 */
public abstract class DialogWindowAdapter implements IDialogWindowListener {

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.controls.dialogwindow.IDialogWindowListener#onDialogAborted(com.netapp.pulse.basegui.controls.dialogwindow.DialogEvent)
	 */
	@Override
	public void onDialogAborted(DialogEvent event) {
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.controls.dialogwindow.IDialogWindowListener#onDialogOk(com.netapp.pulse.basegui.controls.dialogwindow.DialogEvent)
	 */
	@Override
	public void onDialogOk(DialogEvent event) {
	}

}
