/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.ecolib.toolbar.ToolbarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.util.SerObserver;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Abstract list container for baseviews showing simple lists.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public abstract class AbstractListContainer extends BaseView implements SerObserver {

	protected Button btEdit;
	protected Button btDelete;
	protected Button btNew;

	protected ListModel listModel = null;

	/**
	 * Creates the abstract list container for baseviews showing simple lists.
	 * 
	 * @param container
	 * @param name
	 */
	public AbstractListContainer(IControlContainer container, String name, ListModel listModel) {
		this(container, name, listModel, true);
	}

	public AbstractListContainer(IControlContainer container, String name, ListModel listModel, boolean showActions) {
		super(container, name);
		this.listModel = listModel;
		this.listModel.addObserver(this);
		
		if (showActions) {
			setupActionBar();
		}
	}

	@Override
	public void destroy() {
		if (listModel != null) {
			listModel.deleteObserver(this);
		}
		
		super.destroy();
	}
	
	/**
	 * Creates the Actionbar
	 */
	@SuppressWarnings("serial")
	protected void setupActionBar() {
		Bundle bundle = BundleAccessor.getDomainBundle(this, ExtendedApplication.CORE_DOMAIN_ID);
		
		ToolbarGroup tg = toolbar.addGroup();
		
		btNew = tg.addButton();
		btNew.setTitle(bundle.getString("baselist.action.new.title"));
		btNew.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				performNew();
			}
		});
		btNew.setIconEnabled(ImageLibrary.ICON_NEW_ACTIVE);
		btNew.setIconDisabled(ImageLibrary.ICON_NEW_INACTIVE);

		btEdit = tg.addButton();
		btEdit.setTitle(bundle.getString("baselist.action.edit.title"));
		btEdit.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				performEdit();
			}
		});
		btEdit.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		btEdit.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);

		btDelete = tg.addButton();
		btDelete.setTitle(bundle.getString("baselist.action.delete.title"));
		btDelete.setConfirmMsg(bundle.getString("baselist.action.delete.confirm"));
		btDelete.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				performDelete();
			}
		});
		btDelete.setIconEnabled(ImageLibrary.ICON_DELETE_ACTIVE);
		btDelete.setIconDisabled(ImageLibrary.ICON_DELETE_INACTIVE);
	}

	/**
	 * Called, when the buttons need a status refresh.
	 */
	protected void updateButtonStatus() {
		if (btNew != null) {
			btNew.setEnabled(canNew());
			btEdit.setEnabled(canEdit());
			btDelete.setEnabled(canDelete());
		}

	}

	/**
	 * Called, when the Delete-Button was pressed.
	 * <p>
	 */
	public abstract void performDelete();

	/**
	 * Called, when the Edit-Button was pressed.
	 * <p>
	 * 
	 * @return true, if everything went well
	 */
	public abstract boolean performEdit();

	/**
	 * Called, when the New Button was pressed.
	 */
	public abstract void performNew();

	/**
	 * Check, if new button can be pressed.
	 * 
	 * @return boolean
	 */
	public boolean canNew() {
		return listModel.canNew();
	}

	/**
	 * Check, if edit button can be pressed.
	 * 
	 * @return boolean
	 */
	public boolean canEdit() {

		return listModel.canEdit();
	}

	/**
	 * Check, if delete button can be pressed.
	 * 
	 * @return boolean
	 */
	public boolean canDelete() {
		return listModel.canDelete();
	}

}
