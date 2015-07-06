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
package de.xwic.appkit.core.ucexec;

import java.util.Date;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.UseCase;


/**
 * Use case for retreiving the date from the serverside.
 *
 * @author Aron Cotrau
 */
public class GetServerDateUseCase extends UseCase {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.UseCase#execute(de.xwic.appkit.core.dao.DAOProviderAPI)
	 */
	protected Object execute(DAOProviderAPI api) {
		Date date = new Date();
		
		// return the current date from the server
		return date;
	}

}
