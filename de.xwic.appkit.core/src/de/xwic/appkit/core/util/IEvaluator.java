package de.xwic.appkit.core.util;


/**
 * @author Alexandru Bledea
 * @since Jul 9, 2013
 */
public interface IEvaluator<Obj, ReturnValue> extends IEvaluatorWithException<Obj, ReturnValue> {

	/* (non-Javadoc)
	 * @see com.notbed.util.map.IEvaluatorWithException#evaluate(java.lang.Object)
	 */
	@Override
	ReturnValue evaluate(Obj obj);

}
