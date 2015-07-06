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
package de.xwic.appkit.core.security.util;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.util.Function;


/**
 * @author Alexandru Bledea
 * @since Oct 17, 2014
 */
public final class Rights {

	public static final Function<IRight, IRole> GET_ROLE = new ExtractRoleFromRight();

	public static final Function<IRight, IAction> GET_ACTION = new ExtractActionFromRight();

	/**
	 *
	 */
	private Rights() {
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 17, 2014
	 */
	private static class ExtractRoleFromRight implements Function<IRight, IRole> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public IRole evaluate(final IRight obj) {
			return obj.getRole();
		}

	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 17, 2014
	 */
	private static class ExtractActionFromRight implements Function<IRight, IAction> {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public IAction evaluate(final IRight obj) {
			return obj.getAction();
		}

	}

}
