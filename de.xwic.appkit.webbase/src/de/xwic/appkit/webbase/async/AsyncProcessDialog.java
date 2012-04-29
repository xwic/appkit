package de.xwic.appkit.webbase.async;

import de.jwic.ecolib.async.IAsyncProcess;
import de.jwic.ecolib.async.IProcessListener;
import de.jwic.ecolib.async.ProcessEvent;
import de.jwic.ecolib.async.ProcessInfo;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Starts a process and displays the status in a modal dialog.
 * 
 * @author lippisch
 */
public class AsyncProcessDialog extends AbstractDialogWindow {

	private final IAsyncProcess process;
	private ProcessInfo processInfo;
	
	private boolean autoClose = true;

	/**
	 * Construct a new dialog with a process. Call the start() method to
	 * open the dialog and start the process in a background thread.
	 * @param site
	 * @param process
	 */
	public AsyncProcessDialog(Site site, IAsyncProcess process) {
		super(site);
		this.process = process;
		setWidth(412);
		setHeight(180);
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
		processInfo.globalRefresh();
		processInfo.stopRefresh();
		
		if (process.getResult() instanceof Throwable) { // an error occured
			autoClose = false;
			getSessionContext().notifyMessage("Error: " + process.getResult(), "xwic-notify-warning");
		}
		
		if (autoClose) {
			close();
		} else {
			btCancel.setEnabled(true);
			btCancel.setTitle("Close");
		}
		
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

}
