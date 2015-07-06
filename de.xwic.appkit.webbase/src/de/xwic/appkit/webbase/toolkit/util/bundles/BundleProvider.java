/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.Setup;
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
		
		if (bundles.containsKey(langId)) {
			return bundles.get(langId);
		} else {
			// fall back on the default language if the langId is not supported
			
			String defaultLangId = "";
			
			Setup setup = ConfigurationManager.getSetup();
			if (setup != null) {
				defaultLangId = setup.getDefaultLangId();
			}
			
			if (defaultLangId == null || defaultLangId.trim().isEmpty()) {
				defaultLangId = "en";
			}
			
			return bundles.get(defaultLangId);
		}		
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
