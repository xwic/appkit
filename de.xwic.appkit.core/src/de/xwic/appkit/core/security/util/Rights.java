/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * src.de.xwic.appkit.core.security.util.Rights
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

	public static final Function<IRight, IRole> EXTRACT_ROLE = new ExtractRoleFromRight();

	public static final Function<IRight, IAction> EXTRACT_ACTION = new ExtractActionFromRight();

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
