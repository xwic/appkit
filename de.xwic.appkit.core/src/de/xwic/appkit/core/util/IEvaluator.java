package de.xwic.appkit.core.util;


/**
 * Use this if you have a portion of code that may be executed depending on the circumstances.<br>
 * for instance<br>
 * if (condition1 && condition2){<br>
 * &nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;if (condition3){<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<b>x = evaluator.evaluate(x)</b>;<br>
 * &nbsp;&nbsp;}
 * &nbsp;&nbsp;...
 *
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
