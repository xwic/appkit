/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Aug 22, 2013
 */
public abstract class VoidEvaluator<O> implements Function<O, Void> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.IEvaluator#evaluate(java.lang.Object)
	 */
	@Override
	public final Void evaluate(O obj) {
		evaluateNoResult(obj);
		return null;
	}

	/**
	 * @param obj
	 */
	public abstract void evaluateNoResult(O obj);

}
