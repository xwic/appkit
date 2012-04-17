/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.quickfilter;

import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * @author Adrian Ionescu
 */
public interface IQuickFilterControl {
	
	/**
	 * @param column
	 */
	public void columnFilterChanged(Column column);
	
	/**
	 * @return
	 */
	public QueryElement getQueryElement();
	
	/**
	 * @return
	 */
	public String getPropertyId();
}
