package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Sep 26, 2013
 * @param <O>
 */
public interface ILazyStringEval<O> extends ILazyEval<O, String> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.ILazyEval#evaluate(java.lang.Object)
	 */
	@Override
	String evaluate(O obj);

}
