/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Jul 14, 2013
 */
public class DuplicateKeyException extends Exception {

	/**
	 * @param evaluate
	 */
	public <K> DuplicateKeyException(K evaluate) {
		super("Duplicate key found: ".concat(String.valueOf(evaluate)));
	}

}
