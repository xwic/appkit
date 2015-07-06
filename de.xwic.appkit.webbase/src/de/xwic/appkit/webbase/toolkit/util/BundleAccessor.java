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
package de.xwic.appkit.webbase.toolkit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.Control;
import de.jwic.base.SessionContext;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Domain;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Helper for getting the Bundle from the configuration.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class BundleAccessor {

	private final static Log log = LogFactory.getLog(BundleAccessor.class);
	
	/**
	 * Get Domain bundle.
	 * 
	 * @param control
	 * @param domainId
	 * @return
	 */
    public static Bundle getDomainBundle(Control control, String domainId) {
        Domain domain = null;
        Bundle bundle = null;
		try {
			domain = ConfigurationManager.getSetup().getDomain(domainId);
			bundle = domain.getBundle(control.getSessionContext().getLocale().getLanguage());			
		} catch (ConfigurationException e) {
			log.error(e);
		}
        return bundle;
    }
    
    /**
     * Get Domain bundle.
     * 
     * @param context
     * @param domainId
     * @return
     */
    public static Bundle getDomainBundle(SessionContext context, String domainId) {
        Domain domain = null;
        Bundle bundle = null;
		try {
			domain = ConfigurationManager.getSetup().getDomain(domainId);
			bundle = domain.getBundle(context.getLocale().getLanguage());			
		} catch (ConfigurationException e) {
			log.error(e);
		}
        return bundle;
    }

    /**
     * Get Domain bundle.
     * 
     * @param langId
     * @param domainId
     * @return
     */
    public static Bundle getDomainBundle(String langId, String domainId) {
        Domain domain = null;
        Bundle bundle = null;
		try {
			domain = ConfigurationManager.getSetup().getDomain(domainId);
			bundle = domain.getBundle(langId);			
		} catch (ConfigurationException e) {
			log.error(e);
		}
        return bundle;
    }

    /**
     * Returns the description of the given filed and class type.
     * 
     * @param clazz
     * @param langId
     * @param fieldName
     * @return
     */
    public static String getBundleStringByType(Class<? extends IEntity> clazz, String langId, String fieldName) {
        Setup setup = ConfigurationManager.getSetup();
        
        Bundle edBundle = null;
		try {
	        EntityDescriptor ed = setup.getEntityDescriptor(clazz.getName());
	        edBundle = ed.getDomain().getBundle(langId);
		} catch (ConfigurationException e) {
			log.error(e);
			return "No bundle found for class: " + clazz;
		}
        return fieldName != null ? edBundle.getString(clazz.getName() + "." + fieldName) : edBundle.getString(clazz.getName());
    }
    
}
