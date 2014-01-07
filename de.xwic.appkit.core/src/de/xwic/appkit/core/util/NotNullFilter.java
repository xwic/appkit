/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * By default it returns all elements that are not null, you can override the keepNotNull method for more filtering
 * @author Alexandru Bledea
 * @since Jan 6, 2014
 */
public class NotNullFilter<E> implements IFilter<E> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.IFilter#keep(java.lang.Object)
	 */
	@Override
	public final boolean keep(E element) {
		if (element == null) {
			return false;
		}
		return keepNotNull(element);
	}

	/**
	 * @param element
	 * @return
	 */
	protected boolean keepNotNull(E element) {
		return true;
	}

}
