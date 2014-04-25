/**
 *
 */
package de.xwic.appkit.webbase.toolkit.util.bundles;

import java.util.Collection;

import de.xwic.appkit.core.config.Language;

/**
 * @author Adrian Ionescu
 */
public interface ILanguageProvider {

	/**
	 * @param langId
	 * @return
	 */
	public Collection<Language> getLanguages();
	
	/**
	 * @param id
	 * @return
	 */
	public Language getLanguageById(String id);

	/**
	 * @param langId
	 * @return
	 */
	public boolean hasLanguage(String langId);	
}
