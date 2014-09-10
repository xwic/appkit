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
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.Label;
import de.jwic.controls.StackedContainer;
import de.jwic.controls.dialogs.DialogEvent;
import de.jwic.controls.dialogs.DialogListener;
import de.jwic.controls.wizard.AbstractWizard;
import de.jwic.controls.wizard.ValidationException;
import de.jwic.controls.wizard.WizardPage;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.util.Messages;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;

/**
 * Parameterized wizard container
 *
 * @author Alexandru Bledea
 * @since Sep 10, 2014
 */
public class WizardContainer<WP extends WizardPage, W extends AbstractWizard<WP>> {

	private IControlContainer parent;
	private ControlContainer container;
	private List listeners;
	
	protected Button btBack;
	protected Button btNext;
	protected Button btFinish;
	protected Button btAbort;
	
	private Label lblPageTitle;
	private Label lblPageSubTitle;
	
	private ErrorWarning errorWarning;
	
	private StackedContainer pages;
	
	private W wizard;
	private WP currentPage;
	
	private Map pageMap = new HashMap();

	
	
	public WizardContainer(IControlContainer parent, W wizard) {
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

		new Messages(container.getSessionContext().getLocale(), "de.jwic.controls.wizard.messages");
		
		InnerPage win = new InnerPage(container, null);
		win.setTitle(wizard.getTitle());

		if(wizard.getWidth() > 0){
			win.setMaxWidth(wizard.getWidth());
		}else {
			// default width
			win.setMaxWidth(900);
		}
		
		ControlContainer winContainer = new ControlContainer(win);
		winContainer.setTemplateName(getContainerTemplate());
		
		lblPageTitle = new Label(winContainer, "lblPageTitle");
		lblPageTitle.setCssClass("title");
		lblPageSubTitle = new Label(winContainer, "lblPageSubTitle");
		lblPageSubTitle.setCssClass("subtitle");
		
		errorWarning = new ErrorWarning(winContainer, "errorWarning");
		errorWarning.setAutoClose(true);
		
		pages = new StackedContainer(winContainer, "pages");
//		pages.setWidth(wizard.getWidth());
//		pages.setHeight(wizard.getHeight());
//		
		
		NavigationController navContr = new NavigationController();
		
		btBack = new Button(winContainer, "btBack");
		btBack.setTitle("Back"); // messages.getString("wizard.button.back"));
		btBack.addSelectionListener(navContr);
		
		btNext = new Button(winContainer, "btNext");
		btNext.setTitle("Next"); // messages.getString("wizard.button.next"));
		btNext.addSelectionListener(navContr);

		btFinish = new Button(winContainer, "btFinish");
		btFinish.setTitle("Finish"); // messages.getString("wizard.button.finish"));
		btFinish.addSelectionListener(navContr);

		btAbort = new Button(winContainer, "btAbort");
		btAbort.setTitle("Close"); // messages.getString("wizard.button.abort"));
		btAbort.addSelectionListener(navContr);

		addMoreButtons(winContainer);

		activatePage(currentPage);
		
	}

	/**
	 * @param toolbar
	 */
	protected void addMoreButtons(final IControlContainer toolbar) {
	}

	/**
	 * @return
	 */
	protected String getContainerTemplate() {
		return WizardContainer.class.getName();
	}

	/**
	 * @param page
	 */
	private void activatePage(WP page) {
		onActivatePage(errorWarning);
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
	 * i'm here if you need me...
	 *
	 * @param errorWarning
	 */
	protected void onActivatePage(ErrorWarning errorWarning) {
	}

	/**
	 * Navigate back.
	 */
	protected void performBack () {
	
		if (wizard.hasPrevious(currentPage)) {
			WP newPage = wizard.getPreviousPage(currentPage);
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
				WP newPage = wizard.getNextPage(currentPage);
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
	public W getWizard() {
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
	 * @return the currentPage
	 */
	protected final WizardPage getCurrentPage() {
		return currentPage;
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
	public Button getBtBack() {
		return btBack;
	}

	/**
	 * @return the btNext
	 */
	public Button getBtNext() {
		return btNext;
	}

	/**
	 * @return the btFinish
	 */
	public Button getBtFinish() {
		return btFinish;
	}

	/**
	 * @return the btAbort
	 */
	public Button getBtAbort() {
		return btAbort;
	}
}
