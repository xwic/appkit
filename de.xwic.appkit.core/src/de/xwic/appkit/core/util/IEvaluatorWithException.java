package de.xwic.appkit.core.util;


/**
 * @author Alexandru Bledea
 * @since Jul 9, 2013
 */
public interface IEvaluatorWithException<Obj, ReturnValue> {

	/**
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	ReturnValue evaluate(Obj obj) throws Exception;

}
