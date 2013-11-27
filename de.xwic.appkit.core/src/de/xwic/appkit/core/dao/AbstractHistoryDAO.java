/**
 *
 */
package de.xwic.appkit.core.dao;

/**
 * @author jbornema
 *
 */
public abstract class AbstractHistoryDAO<I extends IEntity, E extends Entity> extends AbstractDAO<I, E> {

	{
		// by default enable history handling
		setHandleHistory(true);
	}

	/**
	 * @param iClass
	 * @param eClass
	 */
	public AbstractHistoryDAO(Class<I> iClass, Class<E> eClass) {
		super(iClass, eClass);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getHistoryImplClass()
	 */
	@Override
	public abstract Class<? extends IHistory> getHistoryImplClass();
}
