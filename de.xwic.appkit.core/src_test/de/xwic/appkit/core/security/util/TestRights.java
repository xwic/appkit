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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.util.CollectionUtil;

/**
 *
 */



/**
 * @author Alexandru Bledea
 * @since Oct 17, 2014
 */
@SuppressWarnings("static-method")
public class TestRights {

	/**
	 *
	 */
	@Test
	public void testEvaluators() {
		final IAction action = Mockito.mock(IAction.class);
		final IRole role = Mockito.mock(IRole.class);
		final IRight createRight = createRight(action, role);
		final IAction actualAction = CollectionUtil.evaluate(createRight, Rights.GET_ACTION, null);
		Assert.assertSame(action, actualAction);

		final IRole actualRole = CollectionUtil.evaluate(createRight, Rights.GET_ROLE, null);
		Assert.assertSame(role, actualRole);
	}

	/**
	 * @param action
	 * @param role
	 * @return
	 */
	private static IRight createRight(final IAction action, final IRole role) {
		final IRight mock = Mockito.mock(IRight.class);
		Mockito.when(mock.getAction()).thenReturn(action);
		Mockito.when(mock.getRole()).thenReturn(role);
		return mock;
	}
}
