/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.QuickLaunchPanel 
 */
package de.xwic.appkit.webbase.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jwic.base.ControlContainer;
import de.jwic.base.Field;
import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.AnchorLink;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.entities.IQuickLaunch;
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.DialogWindowAdapter;
import de.xwic.appkit.webbase.home.ql.IQuickLaunchFunction;
import de.xwic.appkit.webbase.home.ql.QuickLaunchConfigDialog;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * @author lippisch
 *
 */
@JavaScriptSupport
public class QuickLaunchPanel extends ControlContainer {

	private QuickLaunchModel model;
	private Site site;
	private Field qlItemOrder; 
	private Map<String,IQuickLaunchFunction> cardFunctionMap = new HashMap<String, IQuickLaunchFunction>();
	private String appId;
	
	/**
	 * @param container
	 * @param name
	 */
	public QuickLaunchPanel(IControlContainer container, String name, String appId) {
		super(container, name);
		this.appId = appId;

		site = ExtendedApplication.getInstance(this).getSite();
		model = new QuickLaunchModel(appId);
		model.initUser(site);
		
		qlItemOrder = new Field(this, "qlItemOrder");
		qlItemOrder.addValueChangedListener(new ValueChangedListener() {
			
			@Override
			public void valueChanged(ValueChangedEvent event) {
				onCardOrderChanged();
			}
		});
		
		AnchorLink linkConfig = new AnchorLink(this, "linkConfig");
		linkConfig.setTitle("Add/Remove Quick Launch Cards");
		linkConfig.addSelectionListener(new SelectionListener() {
			
			@Override
			public void objectSelected(SelectionEvent event) {
				onConfigCards();
			}
		});

	}
	
	/**
	 * The order of the cards has been modified. The field contains the ids
	 * in order how the user selected them. Simply iterate over the ids and update
	 * the order accordingly. 
	 */
	protected void onCardOrderChanged() {
		
		String val = qlItemOrder.getValue().trim();
		if (!val.isEmpty()) { // empty means it was never modified/cleared
			String[] ids = val.split(";");
			model.reIndex(ids);
			
		}
		
	}

	/**
	 * 
	 */
	protected void onConfigCards() {

		QuickLaunchConfigDialog qlConfDlg = new QuickLaunchConfigDialog(site, model, appId);
		qlConfDlg.addDialogWindowListener(new DialogWindowAdapter() {
			@Override
			public void onDialogOk(DialogEvent event) {
				requireRedraw();
			}
		});
		qlConfDlg.show();
		
	}

	/**
	 * Return the quickLaunchItems.
	 * @return
	 */
	public List<QuickLaunchUIItem> getQuickLaunchItems() {
		List<QuickLaunchUIItem> items = new ArrayList<QuickLaunchUIItem>();
		
		for (IQuickLaunch itm : model.getQuickLaunchItems()) {
			
			// load by reference
			if (itm.getReference().startsWith(QuickLaunchModel.RESTYPE_SUBMODULE)) {
				loadSubmodule(items, itm);
			}else if(itm.getReference().startsWith(QuickLaunchModel.RESTYPE_FUNCTION)){
				loadFunction(items, itm);
			}
		}
		return items;
	}

	/**
	 * @param items
	 * @param itm
	 */
	private void loadFunction(List<QuickLaunchUIItem> items, IQuickLaunch itm) {
		List<IQuickLaunchFunction> functions = model.getFunctions();
		for(IQuickLaunchFunction fct:functions){
			if(fct.getReference().equals(itm.getReference()) && fct.isAvailable(DAOSystem.getSecurityManager().getCurrentUser())) {
				QuickLaunchUIItem uiItem = new QuickLaunchUIItem();
				uiItem.setReference(fct.getReference());
				uiItem.setContent(fct.getDescription());
				uiItem.setTitle(fct.getTitle());
				uiItem.setIcon(fct.getIcon());
				uiItem.setQuickLinkId(itm.getId());
				items.add(uiItem);
				cardFunctionMap.put(fct.getReference(), fct);
			}
		}
	}

	/**
	 * @param items
	 * @param itm
	 */
	private void loadSubmodule(List<QuickLaunchUIItem> items, IQuickLaunch itm) {
		String path = itm.getReference().substring(QuickLaunchModel.RESTYPE_SUBMODULE.length() + 1);
		String[] keys = path.split("/");
		try {
			if (keys.length > 1) {
				Module module = site.getModuleByKey(keys[0]);
				SubModule sm = module.getSubModule(keys[1]);
				// iterate for sub-sub modules
				for (int i = 2; i < keys.length; i++) {
					sm = sm.getSubModule(keys[i]);
				}
				
				// add the sub module
				QuickLaunchUIItem uiItem = new QuickLaunchUIItem(sm);
				uiItem.setQuickLinkId(itm.getId());
				uiItem.setReference(itm.getReference());
				items.add(uiItem);
			}
		} catch (RuntimeException re) {
			// this is thrown when a module is (no longer) available
			// in that case, do nothing and simply do not show the ql
		}
	} 
	
	/**
	 * Invoked when a link is clicked.
	 * @param reference
	 */
	public void actionLaunch(String reference) {

		int idx = reference.indexOf(':');
		if (idx != -1) {
			String type = reference.substring(0, idx);
			String path = reference.substring(idx + 1);
			if (type.equals(QuickLaunchModel.RESTYPE_SUBMODULE)) {
				// do not switch "ourself" but do it via the menu function
				getSessionContext().queueScriptCall("mainMenu.select('" + path.replace('/', ';') + "', 1)");
			}else if (type.equals(QuickLaunchModel.RESTYPE_FUNCTION)) {
				cardFunctionMap.get(reference).run(this);
			}
		}
	}
}
