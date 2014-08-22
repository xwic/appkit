/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Aug 22, 2014
 */
public final class Evaluators {

	/**
	 *
	 */
	private Evaluators() {
	}

	@SuppressWarnings ("rawtypes")
	private static ILazyEval IDENTITY_EVALUATOR = new ILazyEval() {

		@Override
		public Object evaluate(final Object obj) {
			return obj;
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	public static <E> ILazyEval<E, E> identity() {
		return IDENTITY_EVALUATOR;
	}

}
