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
package de.xwic.appkit.core.model.queries;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * Query for getting a ServerConfigProperty by the given key. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class ServerConfigPropertyByKeyQuery extends EntityQuery {

    private String key = null;
    
    /**
     * Constructor.
     */
    public ServerConfigPropertyByKeyQuery(){
    }
    
    /**
     * Query for getting a ServerConfigProperty by the given key. <p>
     * 
     * @param key of the property
     */
    public ServerConfigPropertyByKeyQuery(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("IllegalArgumentException in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        this.key = key;
    }
    
    /**
     * @return Returns the key.
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
