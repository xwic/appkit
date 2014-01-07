/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Jan 6, 2014
 */
public interface IFilter<E> {

	/**
	 * @param element
	 * @return
	 */
	boolean keep(E element);

}
