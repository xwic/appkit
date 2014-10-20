/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * de.xwic.appkit.core.security.util.Roles
 */
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
