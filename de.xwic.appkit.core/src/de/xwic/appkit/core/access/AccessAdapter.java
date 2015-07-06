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
package de.xwic.appkit.core.access;

/**
 * Adapter for the AccessListener.
 * @author Florian Lippisch
 */
public abstract class AccessAdapter implements AccessListener {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityDeleted(AccessEvent event) {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entitySoftDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entitySoftDeleted(AccessEvent event) {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.access.AccessListener#entityUpdated(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityUpdated(AccessEvent event) {

	}

}
