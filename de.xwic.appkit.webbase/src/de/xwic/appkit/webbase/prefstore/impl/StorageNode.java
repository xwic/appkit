/*
 * Copyright 2005 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.wap.platform.pref.StorageNode
 * Created on 20.01.2006
 * $Id: StorageNode.java,v 1.1 2006/01/25 10:11:46 flippisch Exp $
 */
package de.xwic.appkit.webbase.prefstore.impl;

import java.io.File;
import java.io.IOException;

/**
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public class StorageNode {

	protected String name = null;
	protected File nodePath = null;
	
	/**
	 * Default node.
	 *
	 */
	public StorageNode() {
		
	}
	
	/**
	 * @param path
	 * @param nodename
	 */
	public StorageNode(File path, String nodename) {
		this.name = nodename;
		this.nodePath = path;
	}

	/**
	 * Returns the name of the node.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the node with the specified name. 
	 * 
	 * @param nodeName
	 * @return
	 */
	public StorageNode getNode(String nodename) {
		
		File path = new File(nodePath, nodename);
		if (!path.exists()) {
			if (!path.mkdir()) {
				throw new IllegalStateException("Can't create node - can't write?");
			}
		} else {
			if (!path.isDirectory()) {
				throw new IllegalArgumentException("Illegal nodename - a file with that name already exists.");
			}
		}
		return new StorageNode(path, nodename);
	}
	
	/**
	 * Returns the preference store within this node.
	 * @param name
	 * @return
	 */
	public Storage getStorage(String name) {
		try {
			return new Storage(new File(nodePath, name + ".preferences"));
		} catch (IOException e) {
			throw new RuntimeException("Error accessing preference store", e);
		}
	}

	
}
