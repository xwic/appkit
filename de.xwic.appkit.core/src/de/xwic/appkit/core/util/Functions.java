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
	 * @return a new Function instance representing a sequence call of this and next
	 */
	public <C> Functions<A, C> andThen(final Function<B, C> next){
		return compose(this, next);
	}
	
	/**
	 * Wraps a Function
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


	/**
	 * Represents the neutral operation for function composition: f(x) = x
	 *
	 * @return a function that returns its parameter
	 */
	@SuppressWarnings ("unchecked")
	public static <E> Function<E, E> identity() {
		return IdentityFunction.INSTANCE;
	}

	private static enum IdentityFunction implements Function{
		INSTANCE;
		@Override
		public Object evaluate(Object obj) {
			return obj;
		}

		@Override
		public String toString() {
			return "identity";
		}
	}
	@SuppressWarnings ("unchecked")
	public static <E> Function<E, String> toStringFunction() {
		return ToStringFunction.INSTANCE;
	}

	private static enum ToStringFunction implements Function{
		INSTANCE;

		@Override
		public Object evaluate(Object obj) {
			return String.valueOf(obj);
		}

		@Override
		public String toString() {
			return "toString";
		}
	}

	/**
	 * Composes 2 functions
	 * @param f
	 * @param g
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 * @return
	 */
	public static <A,B,C> Functions<A,C> compose(Function<A,B> f, Function<B,C> g){
		return new FunctionComposition<A,B,C>(f,g);
	}

	/**
	 * A function composition of 2 functions (f: A->B).(g: B->C) = f.g:A -> C
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 */
	private static final class FunctionComposition<A,B,C> extends Functions<A,C>{
		private final Function<A,B> f;
		private final Function<B,C> g;

		private FunctionComposition(Function<A, B> f, Function<B, C> g) {
			this.f = f;
			this.g = g;
		}

		@Override
		public C evaluate(A obj) {
			if(obj == null){
				return null;
			}
			final B evaluate = f.evaluate(obj);
			if(evaluate == null){
				return null;
			}
			return g.evaluate(evaluate);
		}

		@Override
		public String toString() {
			return  g + "(" + f + ")";
		}
	}



}
