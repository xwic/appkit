/*
 * $Id: PicklistEntryControl.java,v 1.1 2008/08/20 18:44:25 ronnyp Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.PicklistControl.java
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.utils.picklist;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ListBoxControl;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * PicklistSingleSelection control.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class PicklistEntryControl extends ListBoxControl implements IPicklistEntryControl {

	String lang = "DE";
	private boolean allowEmptySelection = true;
	private Map<Integer, IPicklistEntry> entries;
	
	private IPicklisteDAO plDao = null;
	private String picklistKey = null;
	
	private Comparator<IPicklistEntry> comparator = null;
	
	private String emptySelectionText = "";
	
	public PicklistEntryControl(IControlContainer container, String name, String picklistKey) {
		this(container, name, picklistKey, true, null);
	}

	public PicklistEntryControl(IControlContainer container, String name, String picklistKey, boolean allowEmptySelection, Comparator<IPicklistEntry> comparator) {
		super(container, name);
		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
		this.picklistKey = picklistKey;
		this.allowEmptySelection = allowEmptySelection;
		setTemplateName(ListBoxControl.class.getName());
		this.lang = getSessionContext().getLocale().getLanguage();
		this.comparator = comparator == null ? new PicklistEntryComparator(lang) : comparator;
		setupEntries(lang, picklistKey);
	}
	
	public PicklistEntryControl(IControlContainer container, String name, List<IPicklistEntry> entryList, boolean allowEmptySelection) {
		super(container, name);
		plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
		this.allowEmptySelection = allowEmptySelection;
		setTemplateName(ListBoxControl.class.getName());
		this.lang = getSessionContext().getLocale().getLanguage();
		this.comparator = new PicklistEntryComparator(lang);
		setupEntries(lang, entryList);
	}


	/**
	 * set an internal array for the given list of entries
	 * @param lang
	 * @param entryList
	 */
	protected void setupEntries(String lang, List<IPicklistEntry> entryList){
		clear();
		this.lang = lang;
		
		
		if (null != entryList){
			entries = new HashMap<Integer, IPicklistEntry>();
			
			for (Iterator<IPicklistEntry> it = entryList.iterator(); it.hasNext(); ) {
				IPicklistEntry entry = (IPicklistEntry)it.next();
				if (!entry.isVeraltet()) {
					entries.put(new Integer(entry.getId()), entry);
					this.picklistKey = entry.getPickliste().getKey();
				}
			}
			
			// sort the list 
			Collections.sort(entryList, comparator);
			
			//add empty selection
			if (allowEmptySelection){
				entries.put(new Integer(0), null);
				addElement(emptySelectionText, "0");
			}
	
			String preSelection = null;
			
			// add entries into combo
			for (int i = 0; i < entryList.size(); i++) {
				IPicklistEntry entry = (IPicklistEntry)entryList.get(i);
				if (!entry.isVeraltet()) {
					addElement(entry.getBezeichnung(lang), Integer.toString(entry.getId()));
				}
				
				if (i == 0) {
					preSelection = Integer.toString(entry.getId());
				}
				
			}
			
			if (allowEmptySelection) {
				setSelectedKey("0");
			} else if (preSelection != null){
				setSelectedKey(preSelection);
			}
		}
	}
	
	
	/**
	 * set an internal array for the given list of entries
	 * @param lang
	 * @param entryList
	 */
	@SuppressWarnings("unchecked")
	protected void setupEntries(String lang, String picklistKey){
		clear();
		this.lang = lang;
		
		List<IPicklistEntry> entryList = plDao.getAllEntriesToList(picklistKey);
		
		setupEntries(lang, entryList);
	}
	
	
	/** select the item corresponding to the given entry id
	 * @param pickEntryId
	 */
	public void selectEntry(IPicklistEntry pEntry){
		if (null != entries && null != pEntry){
			boolean found = false;
			
			if (entries.containsKey(new Integer(pEntry.getId()))) {
				found = true;
				setSelectedKey(Integer.toString(pEntry.getId()));
			}
			
			if (!found && pEntry.isVeraltet()) {
				entries.put(new Integer(pEntry.getId()), pEntry);
				addElement("[" + pEntry.getBezeichnung(lang) + "]", Integer.toString(pEntry.getId()));
				setSelectedKey(Integer.toString(entries.size() - 1));
			}

		}else{
			if (entries!=null && pEntry==null && allowEmptySelection){
				setSelectedKey("0");
			}
		}
		
	} 
	
	/**
	 * @return the id of the selected entry
	 */
	public IPicklistEntry getSelectionEntry(){
		if (getSelectedKey() == null || getSelectedKey().length() < 1) {
			return null;
		}
		
		int i = Integer.parseInt(getSelectedKey());
		
		if (null != entries){		
			if (entries.containsKey(new Integer(i))){
				IPicklistEntry entry = (IPicklistEntry) entries.get(new Integer(i));
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * @return Returns the picklistKey.
	 */
	public String getPicklistKey() {
		return picklistKey;
	}

	/**
	 * @param picklistKey The picklistKey to set.
	 */
	public void setPicklistKey(String picklistKey) {
		this.picklistKey = picklistKey;
	}

	/**
	 * @param allowEmptySelection the allowEmptySelection to set
	 */
	public void setAllowEmptySelection(boolean allowEmptySelection) {
		this.allowEmptySelection = allowEmptySelection;
		setupEntries(lang, picklistKey);
	}

	/**
	 * @return the emptySelectionText
	 */
	public String getEmptySelectionText() {
		return emptySelectionText;
	}

	/**
	 * @param emptySelectionText the emptySelectionText to set
	 */
	public void setEmptySelectionText(String emptySelectionText) {
		this.emptySelectionText = emptySelectionText;
		setupEntries(lang, picklistKey);
	}
	
}
