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
package de.xwic.appkit.webbase.prefstore.impl;

import java.io.File;
import java.util.StringTokenizer;

import de.xwic.appkit.webbase.prefstore.IPreferenceProvider;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;

/**
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public class StorageProvider implements IPreferenceProvider {

	private StorageEngine engine;
	
	/**
	 * Initialize provider at given file path. The path may
	 * contain variable that are replaced by a system property. Sample:
	 * ${rootPath}/storage
	 * @param file
	 */
	public StorageProvider(String filePath) {
		
		if (filePath.startsWith("${")) {
			int idx = filePath.indexOf('}');
			if (idx != -1) {
				String key = filePath.substring(2, idx);
				String value = System.getProperty(key, "");
				filePath = value + filePath.substring(idx + 1);
			}
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir(); // try to create it.
		}
		engine = new StorageEngine(file);
		
	}
	
	/**
	 * Initialize provider at given file location.
	 * @param file
	 */
	public StorageProvider(File file) {
		if (!file.exists()) {
			file.mkdir(); // try to create it.
		}
		engine = new StorageEngine(file);
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceProvider#getPreferenceStore(java.lang.String, java.lang.String)
	 */
	public IPreferenceStore getPreferenceStore(String nodeid, String name) {
		
		StorageNode node = engine;
		StringTokenizer stk = new StringTokenizer(nodeid, "/");
		while (stk.hasMoreTokens()) {
			node = node.getNode(stk.nextToken());
		}
		return node.getStorage(name);
	}

}
