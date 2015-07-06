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
/**
 *
 */
package de.xwic.appkit.core.model.queries;

import org.apache.commons.lang.Validate;

/**
 * @author Alexandru Bledea
 * @since Jun 4, 2014
 */
public class AliasProvider {

	private final String prefix;
	private int counter;

	/**
	 * @param prefix
	 * @throws IllegalArgumentException if the prefix is null
	 */
	public AliasProvider(final String prefix) throws IllegalArgumentException {
		Validate.notNull(prefix, "Missing prefix for the alias provider");
		this.prefix = prefix;
	}

	/**
	 * @return
	 */
	public String nextAlias() {
		return prefix + counter++;
	}

}
