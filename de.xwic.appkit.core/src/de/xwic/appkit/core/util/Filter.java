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
 * @author bogdan
 *
 */
public abstract class Filter<E> implements IFilter<E> {

	/**
	 * 
	 */
	private Filter() {
	}

	/**
	 * Creates a new filter that represents the 'and' operation between this filter and the next<br>
	 * the new Filters keep method will return the equivalent of this.keep(e) && next.keep(e)
	 * @param next
	 * @return
	 */
	public Filter<E> and(final IFilter<E> next){
		return new AndFilter<E>(this, next);
	}
	private static final class AndFilter<E> extends Filter<E>{
		private final IFilter<E> a;
		private final IFilter<E> b;

		private AndFilter(IFilter<E> a, IFilter<E> b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean keep(E element) {
			return a.keep(element) && b.keep(element);
		}

		@Override
		public String toString() {
			return a+ " && " + b;
		}
	}
	
	/**
	 * Creates a new filter that represents the 'or' operation between this filter and the next<br>
	 * the new Filters keep method will return the equivalent of this.keep(e) || next.keep(e)
	 * @param next
	 * @return
	 */
	public Filter<E> or(final IFilter<E> next){
		return new OrFilter<E>(this, next);
	}
	private static final class OrFilter<E> extends Filter<E>{
		private final IFilter<E> a;
		private final IFilter<E> b;

		private OrFilter(IFilter<E> a, IFilter<E> b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean keep(E element) {
			return a.keep(element) || b.keep(element);
		}

		@Override
		public String toString() {
			return a+ " || " + b;
		}
	}
	
	/**
	 * Creates a new Filter that represents 'not this'<br>
	 * The new filter will be the equivalent of<br>
	 * 	 <code>!this.keep(e);</code>
	 * @return
	 */
	public Filter<E> negate(){
		return new NotFilter<E>(this);
	}
	private static final class NotFilter<E> extends Filter<E>{
		private final IFilter<E> a;

		private NotFilter(IFilter<E> a) {
			this.a = a;
		}

		@Override
		public boolean keep(E element) {
			return !a.keep(element);
		}

		@Override
		public String toString() {
			return "!" + a;
		}
	}

	/**
	 * Wraps an IFilter instance
	 * @param wrapped
	 * @return
	 */
	public static <T> Filter<T> of(final IFilter<T> wrapped){
		return new Wrapper<T>(wrapped);
	}
	private static class Wrapper<E> extends Filter<E>{

		private final IFilter<E> wrapped;

		public Wrapper(IFilter<E> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean keep(E element) {
			return wrapped.keep(element);
		}

		@Override
		public String toString() {
			return wrapped.toString();
		}
	}

	@SuppressWarnings("unchecked")
	public static <E> Filter<E> notNullFilter(){
		return NotNullFilter.INSTANCE;
	}

	private static final class NotNullFilter extends Filter{
		static final NotNullFilter INSTANCE = new NotNullFilter();
		@Override
		public boolean keep(Object element) {
			return element != null;
		}

		@Override
		public String toString() {
			return "notNullFilter";
		}
	}

}
