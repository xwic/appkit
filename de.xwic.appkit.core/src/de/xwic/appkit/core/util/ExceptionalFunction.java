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
public interface ExceptionalFunction<O, R, X extends Exception> {

	/**
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	R evaluate(final O obj) throws X;

}
