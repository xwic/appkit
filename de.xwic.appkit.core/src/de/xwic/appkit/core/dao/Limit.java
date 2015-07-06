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
/*
 * de.xwic.appkit.core.dao.Limit
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

/**
 * Specify a range of objects in a list that should be returned. This
 * tells the DAO provider to return only the specified number of 
 * objects inside an result. This helps to make paging more eficient.
 * 
 * @author Florian Lippisch
 */
public class Limit {

	public static final Limit ONE = new Limit(0, 1);

    /** The number to start from. The first entity is 0. */
    public int startNo = 0;
    /** The maximum number of objects that should be returned. 0 means all entities. */
    public int maxResults = 0;
    
    /**
     * Default constructor.
     *
     */
    public Limit() {
        
    }
    
    /**
     * Initialize a new limit that starts at the specified row.
     * @param startNo
     */
    public Limit (int startNo) {
        this.startNo = startNo;
    }
    
    /**
     * Initialize a new limit that starts at the specified row and 
     * returns the specified number of objects.
     * @param startNo
     * @param maxResults
     */
    public Limit (int startNo, int maxResults) {
        this.startNo = startNo;
        this.maxResults = maxResults < 0 ? 0 : maxResults;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return "Limit(" + startNo + ", " + maxResults + ")";
    }

}
