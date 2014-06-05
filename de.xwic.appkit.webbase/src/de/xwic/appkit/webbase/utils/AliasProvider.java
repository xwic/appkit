/**
 *
 */
package de.xwic.appkit.webbase.utils;

import org.apache.commons.lang.Validate;

/**
 * @author Alexandru Bledea
 * @since Jun 4, 2014
 */
public class AliasProvider {

	private final String prefix;
	private int counter;

	/**
	 * @param prefix
	 * @throws IllegalArgumentException if the prefix is null
	 */
	public AliasProvider(final String prefix) throws IllegalArgumentException {
		Validate.notNull(prefix, "Missing prefix for the alias provider");
		this.prefix = prefix;
	}

	/**
	 * @return
	 */
	public String nextAlias() {
		return prefix + counter++;
	}

}
