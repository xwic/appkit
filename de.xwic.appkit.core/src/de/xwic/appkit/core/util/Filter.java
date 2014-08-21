/**
 * 
 */
package de.xwic.appkit.core.util;

import de.xwic.appkit.core.util.IFilter;

/**
 * @author bogdan
 *
 */
public abstract class Filter<E> implements IFilter<E> {

	/**
	 * 
	 */
	public Filter() {
	}

	/**
	 * Creates a new filter that represents the 'and' operation between this filter and the next<br>
	 * the new Filters keep method will return the equivalent of this.keep(e) && next.keep(e)
	 * @param next
	 * @return
	 */
	public Filter<E> and(final IFilter<E> next){
		return new Filter<E>(){

			@Override
			public boolean keep(E element) {
				return getThis().keep(element) && next.keep(element);
			}
			
		};
	}
	
	/**
	 * Creates a new filter that represents the 'or' operation between this filter and the next<br>
	 * the new Filters keep method will return the equivalent of this.keep(e) || next.keep(e)
	 * @param next
	 * @return
	 */
	public Filter<E> or(final IFilter<E> next){
		return new Filter<E> (){
			@Override
			public boolean keep(E element) {
				return getThis().keep(element) || next.equals(element);
			}
		};
	}
	
	/**
	 * Creates a new Filter that represents 'not this'<br>
	 * The new filter will be the equivalent of<br>
	 * 	 <code>!this.keep(e);</code>
	 * @return
	 */
	public Filter<E> negate(){
		return new Filter<E> (){
			@Override
			public boolean keep(E element) {
				return !getThis().keep(element);
			}
		};
	}
	
	/**
	 * @return
	 */
	private Filter<E> getThis(){
		return this;
	}

}
