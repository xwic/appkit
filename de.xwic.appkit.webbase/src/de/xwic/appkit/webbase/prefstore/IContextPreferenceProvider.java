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
package de.xwic.appkit.webbase.prefstore;


/**
 * Provides access to the PreferenceStore in the current context. Can be implemented
 * by the hosting web application to provide context specific preferences stores (i.e. user 
 * based)
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public interface IContextPreferenceProvider {

	/**
	 * Returns a specific storage. 
	 * @return
	 */
	public IPreferenceStore getPreferenceStore(String name);
	
}
