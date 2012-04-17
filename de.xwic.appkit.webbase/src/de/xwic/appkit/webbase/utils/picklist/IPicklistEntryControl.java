package de.xwic.appkit.webbase.utils.picklist;

import de.jwic.base.IControl;

/**
 * Created on Mar 9, 2008
 * @author Ronny Pfretzschner
 */
public interface IPicklistEntryControl extends IControl {

	/**
	 * @return Returns the picklistKey.
	 */
	public String getPicklistKey();

	/**
	 * @param picklistKey The picklistKey to set.
	 */
	public void setPicklistKey(String picklistKey);

}
