/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.IControlContainer;

/**
 * Interface for an control, which acts like a page.
 * Has a title.
 * 
 * @author Ronny Pfretzschner
 *
 */
public interface IPageControl extends IControlContainer {

	/**
	 * @param title to set
	 */
	public void setTitle(String title);
	
	/**
	 * @return the title
	 */
	public String getTitle();
}
