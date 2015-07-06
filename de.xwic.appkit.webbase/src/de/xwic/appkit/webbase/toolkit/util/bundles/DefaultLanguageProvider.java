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

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.util.bundles.ILanguageProvider#hasLanguage(java.lang.String)
	 */
	@Override
	public boolean hasLanguage(String langId) {
		return languages.containsKey(langId);
	}
}
