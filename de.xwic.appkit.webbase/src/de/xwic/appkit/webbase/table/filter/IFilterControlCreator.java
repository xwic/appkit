/**
 * 
 */
package de.xwic.appkit.webbase.table.filter;

import de.jwic.base.IControlContainer;

/**
 * @author Adrian Ionescu
 */
public interface IFilterControlCreator {

	/**
	 * 
	 */
	public AbstractFilterControl createFilterControl(IControlContainer container, String name);
}
