/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.util.Locale;

/**
 * @author Ronny Pfretzschner
 *
 */
public class LocaleFactory {

	/**
	 * @param language
	 * @return the Locale for the given language in format DE, EN etc
	 */
	public static Locale getLocaleForLanguage(String language) {
		
		if ("EN".equalsIgnoreCase(language)) {
			return Locale.US;
		} else if ("DE".equalsIgnoreCase(language)) {
			return Locale.GERMANY;
		}
		
		return Locale.getDefault();
	}
}
