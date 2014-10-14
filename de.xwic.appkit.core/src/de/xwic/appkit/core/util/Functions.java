/**
 * 
 */
package de.xwic.appkit.core.util;


/**
 * A composable LazyEval implementation
 * @author boogie
 * @since  Aug 18, 2014
 */
public abstract class Functions<A, B> implements Function<A, B> {

	private Functions() {
	}
	
	/**
	 * Returns a new Function that represents calling this and the next eval in sequence
	 * <pre>
	 * 		//equivalent to this
	 * 		final B evaluate = this.evaluate(param);
	 *		if(evaluate == null){
	 *			return null;
	 *		}
	 *		return next.evaluate(evaluate);
	 * </pre>
	 * The new evaluator will return null of any of the evaluators returns null
	 * 
	 * @param next - the next evaluator in the chain
	 * @return a new LazyEval instance representing a sequence call of this and next
	 */
	public <C> Functions<A, C> andThen(final Function<B, C> next){
		return new Functions<A, C>() {
			@Override
			public C evaluate(A obj) {
				final B evaluate = Functions.this.evaluate(obj);
				if(evaluate == null){
					return null;
				}
				return next.evaluate(evaluate);
			}
		};
	}
	
	/**
	 * Wraps an ILazyEval
	 * @param eval
	 * @return
	 */
	public static <A, B> Functions<A, B> of(final Function<A, B> eval){
		return new Functions<A, B>() {
			@Override
			public B evaluate(A obj) {
				return eval.evaluate(obj);
			}
		};
	}

}
