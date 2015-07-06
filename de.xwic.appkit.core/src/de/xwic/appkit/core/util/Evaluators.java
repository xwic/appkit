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
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Aug 22, 2014
 * @deprecated - use de.xwic.appkit.core.util.Functions instead
 */
@Deprecated()
public final class Evaluators {

	/**
	 *
	 */
	private Evaluators() {
	}

	@SuppressWarnings ("rawtypes")
	private static Function IDENTITY_EVALUATOR = new Function() {

		@Override
		public Object evaluate(final Object obj) {
			return obj;
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	public static <E> Function<E, E> identity() {
		return IDENTITY_EVALUATOR;
	}

}
