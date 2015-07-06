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


/**
 * @author Alexandru Bledea
 * @since Jun 2, 2014
 */
final class AliasHelper {

	private final String property;
	private final boolean requiresAlias;
	private final String joinProperty;

	/**
	 * @param property
	 * @return
	 */
	public static AliasHelper from(final String property) {
		if (property == null) {
			return new AliasHelper(property, null);
		}

		final int length = property.length();
		final int lastIndexOf = property.lastIndexOf('.');
		if (lastIndexOf < 1 || lastIndexOf == length - 1) { // if not found or if the property starts or ends with a dot
			return new AliasHelper(property, null);
		}
		final String joinProperty = property.substring(0, lastIndexOf);
		final String prop = property.substring(lastIndexOf + 1, length);
		return new AliasHelper(prop, joinProperty);
	}

	/**
	 * @param property
	 * @param joinProperty
	 */
	private AliasHelper(final String property, final String joinProperty) {
		this.property = property;
		this.joinProperty = joinProperty;
		this.requiresAlias = joinProperty != null;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return the requiresAlias
	 */
	public boolean isRequiresAlias() {
		return requiresAlias;
	}

	/**
	 * @return the joinProperty
	 */
	public String getJoinProperty() {
		return joinProperty;
	}

}