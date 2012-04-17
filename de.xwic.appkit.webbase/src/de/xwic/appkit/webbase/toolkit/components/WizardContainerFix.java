/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.Page;
import de.jwic.base.SessionContext;
import de.jwic.controls.ButtonControl;
import de.jwic.controls.LabelControl;
import de.jwic.controls.WindowControl;
import de.jwic.ecolib.controls.ErrorWarningControl;
import de.jwic.ecolib.controls.StackedContainer;
import de.jwic.ecolib.dialogs.DialogEvent;
import de.jwic.ecolib.dialogs.DialogListener;
import de.jwic.ecolib.util.ValidationException;
import de.jwic.ecolib.wizard.Wizard;
import de.jwic.ecolib.wizard.WizardPage;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.util.Messages;

/**
 * Fix, because the ecolib container did not work...
 * 
 * @author Ronny Pfretzschner
 *
 */
public class WizardContainerFix {

	private IControlContainer parent;
	private ControlContainer container;
	private List listeners = null;
	
	private ButtonControl btBack = null;
	private ButtonControl btNext = null;
	private ButtonControl btFinish = null;
	private ButtonControl btAbort = null;
	
	private LabelControl lblPageTitle = null;
	private LabelControl lblPageSubTitle = null;
	
	private ErrorWarningControl errorWarning = null;
	
	private StackedContainer pages = null;
	
	private Wizard wizard = null;
	private WizardPage currentPage = null;
	
	private Map pageMap = new HashMap();

	
	
	public WizardContainerFix(IControlContainer parent, Wizard wizard) {
		this.parent = parent;
		this.wizard = wizard;
		currentPage = wizard.createWizardPages(parent.getSessionContext());

	}
	
	/**
	 * Add a DialogListener.
	 * @param listener
	 */
	public synchronized void addDialogListener(DialogListener listener) {
		if (listeners == null) {
			listeners = new ArrayList();
		}
		listeners.add(listener);
	}
	
	/**
	 * Remove a DialogListener instance.
	 * @param listener
	 */
	public synchronized void removeDialogListener(DialogListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	/**
	 * Cleanup resources.
	 */
	protected void destroy() {
		
		
		
	}
	
	/**
	 * Notifies the listeners that the dialog was 'finished' 
	 * and closes the dialog.
	 */
	public void finish() {
		if (listeners != null) {
			DialogEvent event = new DialogEvent(this);
			for (Iterator it = listeners.iterator(); it.hasNext(); ) {
				DialogListener listener = (DialogListener)it.next();
				listener.dialogFinished(event);
			}
		}
		close();
	}

	/**
	 * Notifies the listeners that the dialog was 'aborted' 
	 * and closes the dialog.
	 */
	public void abort() {
		if (listeners != null) {
			DialogEvent event = new DialogEvent(this);
			for (Iterator it = listeners.iterator(); it.hasNext(); ) {
				DialogListener listener = (DialogListener)it.next();
				listener.dialogAborted(event);
			}
		}
		close();
	}

	/**
	 * 
	 */
	public void close() {

		SessionContext sc = parent.getSessionContext();
		if (sc.getTopControl() == container) {
			sc.popTopControl();
		}
		container.destroy();
		destroy();
		
	}

	public Page openAsPage() {
		
		Page page = new Page(parent);
		
		createControls(page);
		parent.getSessionContext().pushTopControl(page);
		container = page;
		
		return page;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.wap.core.Dialog#createControls(de.jwic.base.IControlContainer)
	 */
	protected void createControls(IControlContainer container) {

		Messages messages = new Messages(container.getSessionContext().getLocale(), "de.jwic.ecolib.wizard.messages");
		
		WindowControl win = new WindowControl(container);
		win.setAlign("center");
		win.setTitle(wizard.getTitle());
		win.setTemplateName(getClass().getName());
		win.setWidth("");
		
		lblPageTitle = new LabelControl(win, "lblPageTitle");
		lblPageTitle.setCssClass("title");
		lblPageSubTitle = new LabelControl(win, "lblPageSubTitle");
		lblPageSubTitle.setCssClass("subtitle");
		
		errorWarning = new ErrorWarningControl(win, "errorWarning");
		errorWarning.setAutoClose(true);
		
		pages = new StackedContainer(win, "pages");
		pages.setWidthHint(wizard.getWidthHint());
		pages.setHeightHint(wizard.getHeightHint());
		
		
		NavigationController navContr = new NavigationController();
		
		btBack = new ButtonControl(win, "btBack");
		btBack.setTitle("Back"); // messages.getString("wizard.button.back"));
		btBack.addSelectionListener(navContr);
		
		btNext = new ButtonControl(win, "btNext");
		btNext.setTitle("Next"); // messages.getString("wizard.button.next"));
		btNext.addSelectionListener(navContr);

		btFinish = new ButtonControl(win, "btFinish");
		btFinish.setTitle("Finish"); // messages.getString("wizard.button.finish"));
		btFinish.addSelectionListener(navContr);

		btAbort = new ButtonControl(win, "btAbort");
		btAbort.setTitle("Abort"); // messages.getString("wizard.button.abort"));
		btAbort.addSelectionListener(navContr);

		activatePage(currentPage);
		
	}

	/**
	 * @param currentPage2
	 */
	private void activatePage(WizardPage page) {
		currentPage = page;
		
		lblPageTitle.setText(page.getTitle());
		lblPageSubTitle.setText(page.getSubTitle());
		
		ControlContainer container = (ControlContainer)pageMap.get(page);
		if (container == null) {
			container = new ControlContainer(pages);
			pageMap.put(page, container);
			page.createControls(container);
		}
		
		pages.setCurrentControlName(container.getName());
		
		if (!wizard.hasPrevious(currentPage) && !wizard.hasNext(currentPage)) {
			btBack.setVisible(false);
			btNext.setVisible(false);
		} else {
			btBack.setEnabled(wizard.hasPrevious(currentPage));
			btNext.setEnabled(wizard.hasNext(currentPage));
		}
		
		btFinish.setEnabled(wizard.canFinish(currentPage));
		page.activated();
	}

	/**
	 * Navigate back.
	 */
	protected void performBack () {
	
		if (wizard.hasPrevious(currentPage)) {
			WizardPage newPage = wizard.getPreviousPage(currentPage);
			if (newPage != null) {
				activatePage(newPage);
			}
		}
	}
	
	/**
	 * Navigate to the next page.
	 */
	protected void performNext() {
		
		try {
			if (currentPage.validate() && wizard.hasNext(currentPage)) {
				WizardPage newPage = wizard.getNextPage(currentPage);
				if (newPage != null) {
					activatePage(newPage);
				}
			}
		} catch (ValidationException ve) {
			errorWarning.showError(validationExceptionToString(ve));
		}
		
	}

	private String validationExceptionToString(ValidationException ve) {
		
		StringBuffer excString = new StringBuffer();
		
		if (null == ve || !ve.hasErrors()) {
			if (null != ve) {
				return ve.getMessage();
			} else {
				return "";
			}
		} else if (null != ve) {
			Collection<?> coll = ve.getErrorFields();
			
			for (Object obj : coll) {
				String message = ve.getMessage(obj.toString());
				excString.append(message).append("<br />");
			}
		}
		
		String str = excString.toString();
		return str;
	}
	
	/**
	 * Perform the finish action.
	 *
	 */
	protected void performFinish() {
		try {
			if (currentPage.validate() && wizard.performFinish()) {
				finish();
			}
		} catch (ValidationException ve) {
			errorWarning.showError(validationExceptionToString(ve));
		}
	}
	
	/**
	 * Abort.
	 */
	protected void performAbort() {
		// abort the wizard
		wizard.performAbort();
		abort();
	}
	
	/**
	 * Returns the wizard hosted by this dialog.
	 * @return
	 */
	public Wizard getWizard() {
		return wizard;
	}
	
	/**
	 * Handles navigation-button selections. 
	 * @author Florian Lippisch
	 * @version $Revision: 1.1 $
	 */
	private class NavigationController implements SelectionListener {
		public void objectSelected(SelectionEvent event) {
			if (event.getEventSource() == btBack) {
				performBack();
			} else
			if (event.getEventSource() == btNext) {
				performNext();
			} else
			if (event.getEventSource() == btFinish) {
				performFinish();
			} else
			if (event.getEventSource() == btAbort) {
				performAbort();
			} 
		}
	}

	/**
	 * @return the container
	 */
	public ControlContainer getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(ControlContainer container) {
		this.container = container;
	}

	/**
	 * @return the btBack
	 */
	public ButtonControl getBtBack() {
		return btBack;
	}

	/**
	 * @return the btNext
	 */
	public ButtonControl getBtNext() {
		return btNext;
	}

	/**
	 * @return the btFinish
	 */
	public ButtonControl getBtFinish() {
		return btFinish;
	}

	/**
	 * @return the btAbort
	 */
	public ButtonControl getBtAbort() {
		return btAbort;
	}
}
