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
package de.xwic.appkit.core.security.util;

import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.util.Function;


/**
 * @author Alexandru Bledea
 * @since Oct 17, 2014
 */
public class Roles {

	public static final Function<IRole, String> GET_NAME = new RoleNameExtractor();

	/**
	 *
	 */
	private Roles() {
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 17, 2014
	 */
	private static class RoleNameExtractor implements Function<IRole, String> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public String evaluate(final IRole obj) {
			return obj.getName();
		}

	}

}
