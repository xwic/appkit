/**
 *
 */
package de.xwic.appkit.webbase.toolkit.util.bundles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.SessionContext;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;

/**
 * @author Adrian Ionescu
 */
public class BundleProvider implements IBundleProvider {

	protected final Log log = LogFactory.getLog(getClass());

	private Map<String, Bundle> bundles = new HashMap<String, Bundle>();
	private ILanguageProvider languageProvider;

	/**
	 * @param domain
	 */
	public BundleProvider(String domain) {
		this(domain, DefaultLanguageProvider.instance());
	}
	
	/**
	 * @param domain
	 */
	public BundleProvider(String domain, ILanguageProvider languageProvider) {
		this.languageProvider = languageProvider;
		
		for (Language lang : languageProvider.getLanguages()) {
			Bundle bundle = BundleAccessor.getDomainBundle(lang.getId(), domain);
			bundles.put(lang.getId(), bundle);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.util.bundles.IBundleProvider#getBundle(de.jwic.base.SessionContext)
	 */
	@Override
	public Bundle getBundle(SessionContext sessionContext) {
		String langId = sessionContext.getLocale().getLanguage();
		return getBundle(langId);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.util.bundles.IBundleProvider#getBundle(java.lang.String)
	 */
	@Override
	public Bundle getBundle(String langId) {
		// override with the one from the LanguageProvider
		// this is used only to allow overriding the languages and using a default one for certain bundles
		
		Language langFromProvider = languageProvider.getLanguageById(langId);
		langId = langFromProvider != null ? langFromProvider.getId() : langId;
		
		return bundles.get(langId);		
	}
	
	/**
	 * @param auxBundleProvider
	 */
	protected void merge(BundleProvider auxBundleProvider) {
		for (Entry<String, Bundle> entry : auxBundleProvider.bundles.entrySet()) {
			String langId = entry.getKey();
			Bundle auxBundle = entry.getValue();
			
			Bundle bundle = bundles.get(langId);
			if (bundle == null) {
				bundles.put(langId, auxBundle);
			} else {
				bundle.merge(auxBundle);
			}
		}
	}
}
