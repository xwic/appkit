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
public interface ExceptionalFunction<O, R> {

	/**
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	R evaluate(O obj) throws Exception;

}
