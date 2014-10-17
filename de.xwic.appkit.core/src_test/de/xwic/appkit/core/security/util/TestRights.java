package de.xwic.appkit.core.security.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.util.CollectionUtil;

/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * src.de.xwic.appkit.core.security.util.Rights
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
		final IAction actualAction = CollectionUtil.evaluate(createRight, Rights.EXTRACT_ACTION, null);
		Assert.assertSame(action, actualAction);

		final IRole actualRole = CollectionUtil.evaluate(createRight, Rights.EXTRACT_ROLE, null);
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
