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
package de.xwic.appkit.webbase.async;

import de.jwic.async.IAsyncProcess;
import de.jwic.async.IProcessListener;
import de.jwic.async.ProcessEvent;
import de.jwic.async.ProcessInfo;
import de.jwic.base.JavaScriptSupport;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.IDialogWindowListener;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Starts a process and displays the status in a modal dialog.
 * 
 * @author lippisch
 */
@JavaScriptSupport
public class AsyncProcessDialog extends AbstractDialogWindow {

	private final IAsyncProcess process;
	private ProcessInfo processInfo;
	
	private boolean autoClose = true;
	private boolean closeMe = false;
	private AsyncProcessControlHelper pcHelper;

	/**
	 * Construct a new dialog with a process. Call the start() method to
	 * open the dialog and start the process in a background thread.
	 * @param site
	 * @param process
	 */
	public AsyncProcessDialog(Site site, IAsyncProcess process) {
		super(site);
		this.process = process;
		setWidth(427);
		setHeight(180);
	}
	
	@Override
	public void actionClose() {
		if(process != null&&process.canCancel() && !process.isFinished()){
			process.cancel();
		}
		super.actionClose();
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {
		btOk.setVisible(false);
		btCancel.setVisible(true);
		btCancel.setEnabled(process.canCancel());
		btCancel.setTitle("Cancel");
		
		processInfo = new ProcessInfo(content, "processInfo");
		processInfo.setProgressMonitor(process.getMonitor());
		
		pcHelper = new AsyncProcessControlHelper(content, "controller", this);
		
	}
	
	/**
	 * Start the process and open the dialog.
	 */
	public void start() {
		show();
		
		process.addProcessListener(new IProcessListener() {
			@Override
			public void processFinished(ProcessEvent e) {
				onProcessFinished(e);
			}
		});
		
		Thread t = new Thread(new BackgroundProcessRunnable(process));
		t.start();
		
	}

	/**
	 * @param e
	 */
	protected void onProcessFinished(ProcessEvent e) {
		if (process.getResult() instanceof Throwable) { // an error occured
			autoClose = false;
			getSessionContext().notifyMessage("Error: " + process.getResult(), "xwic-notify-warning");
		}
		
		if (autoClose) {
			pcHelper.setDestroyNow(true);
		} else {
			btCancel.setEnabled(true);
			btCancel.setTitle("Close");
		}

		processInfo.globalRefresh();
		processInfo.stopRefresh();
		
	}

	/** 
	 * Overriding default "Cancel" behavior.
	 */
	@Override
	protected void onCancel() {
		
		if (process.isFinished()) {
			super.onCancel();
		}
		
		if (process.canCancel()) {
			boolean success = process.cancel();
			if (success) {
				btCancel.setEnabled(false);
				autoClose = true; // turn autoclose on, as dialog should close as soon as process is finished.
			}
		}
		
	}
	
	/**
	 * @return the autoClose
	 */
	public boolean isAutoClose() {
		return autoClose;
	}

	/**
	 * @param autoClose the autoClose to set
	 */
	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	/**
	 * @return the closeMe
	 */
	public boolean isCloseMe() {
		return closeMe;
	}

	/**
	 * @param closeMe the closeMe to set
	 */
	public void setCloseMe(boolean closeMe) {
		this.closeMe = closeMe;
	}

}
