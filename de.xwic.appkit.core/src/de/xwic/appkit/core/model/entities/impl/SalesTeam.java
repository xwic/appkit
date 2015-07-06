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
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.ISalesTeam;

/**
 * Implementation for the SalesTeam Business Object. <p>
 * 
 * @author Ronny Pfretzschner
 */
public class SalesTeam extends Entity implements ISalesTeam {

    private String bezeichnung = null;

    /**
	 * ctor. Just for Hibernate and Webservice. <p>
	 * 
	 * Use this Constructor only at emergency because
	 * then you get just an empty object, <br>without check
	 * of the "must to have" properties.
     */
    public SalesTeam(){
    }
    
	/**
	 * Constructor for SalesTeam. <p>
	 * Use this Constructor instead of the default!
	 * 
	 * @param un the bezeichnung of this SalesTeam
	 */
    public SalesTeam(String bezeichnung) {
        if (bezeichnung == null || bezeichnung.length() < 1) {
            throw new IllegalArgumentException("Error in constructor in class: " + getClass().getName() + "! Arguments not allowed!");
        }
        this.bezeichnung = bezeichnung;
    }
    
    
    /**
     * @return Returns the bezeichnung.
     */
    public String getBezeichnung() {
        return bezeichnung;
    }
    /**
     * @param bezeichnung The bezeichnung to set.
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}
