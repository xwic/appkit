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
package de.xwic.appkit.core.config.editor;


/**
 * Time field.
 *
 * @author Aron Cotrau
 * @editortag time
 */
public class ETime extends EField {
	
    boolean seconds = false;
    
    /**
	 * @return Returns the seconds.
	 */
	public boolean getSeconds() {
		return seconds;
	}
	/**
	 * @param seconds The seconds to set.
	 * @default false
	 */
	public void setSeconds(boolean seconds) {
		this.seconds = seconds;
	}
}
