/**
 * 
 */
package de.xwic.appkit.core.dao;

/**
 * @author jbornema
 *
 */
public abstract class AbstractHistoryDAO extends AbstractDAO {

	{
		// by default enable history handling
		setHandleHistory(true);
	}
	
	public abstract Class<? extends IHistory> getHistoryImplClass();
}
