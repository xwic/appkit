/**
 * 
 */
package de.xwic.appkit.core.util;


/**
 * A composable LazyEval implementation
 * @author boogie
 * @since  Aug 18, 2014
 */
public abstract class LazyEval<A, B> implements ILazyEval<A, B> {

	private LazyEval() {
	}
	
	/**
	 * Returns a new LazyEval that represents calling this and the next eval in sequence
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
	public <C> LazyEval<A, C> andThen(final ILazyEval<B, C> next){
		return new LazyEval<A, C>() {
			@Override
			public C evaluate(A obj) {
				final B evaluate = LazyEval.this.evaluate(obj);
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
	public static <A, B> LazyEval<A, B> of(final ILazyEval<A, B> eval){
		return new LazyEval<A, B>() {
			@Override
			public B evaluate(A obj) {
				return eval.evaluate(obj);
			}
		};
	}

}
