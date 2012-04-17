package de.xwic.appkit.webbase.dialog;

/**
 * @author Adrian Ionescu
 */
public interface IDialogWindowListener {

	/**
	 * The ok button was pressed.
	 * @param event
	 */
	public void onDialogOk(DialogEvent event);
	
	/**
	 * Canceled was pressed or the dialog was destroyed, i.e. by closing the window.
	 * @param event
	 */
	public void onDialogAborted(DialogEvent event);
	
}
