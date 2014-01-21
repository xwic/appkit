/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.util.bundles;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;


/**
 * @author Adrian Ionescu
 */
public class DefaultLanguageProvider implements ILanguageProvider {

	private Map<String, Language> languages;
	
	private static DefaultLanguageProvider instance;

	/**
	 * 
	 */
	protected DefaultLanguageProvider() {
		languages = new HashMap<String, Language>();
		
		for (Language lang : ConfigurationManager.getSetup().getLanguages()) {
			languages.put(lang.getId(), lang);
		}
	}

	/**
	 * @return
	 */
	public static DefaultLanguageProvider instance() {
		if (instance == null) {
			instance = new DefaultLanguageProvider();
		}
		
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.util.ui.bundles.IBundleLanguageProvider#getLanguages()
	 */
	@Override
	public Collection<Language> getLanguages() {
		return languages.values();
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.util.ui.bundles.IBundleLanguageProvider#getLanguageById(java.lang.String)
	 */
	@Override
	public Language getLanguageById(String id) {
		return languages.get(id);
	}

}
