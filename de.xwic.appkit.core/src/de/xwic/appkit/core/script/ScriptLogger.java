/*
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
**/
package de.xwic.appkit.core.script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Script extension to allow simple logging.
 * @author Lippisch
 */
public class ScriptLogger {

	private Log log = LogFactory.getLog(getClass());
	private String logInfo;
	
	/**
	 * 
	 */
	public ScriptLogger(String logInfo) {
		this.logInfo = "[" + logInfo + "]: ";
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isFatalEnabled()
	 */
	public boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	/**
	 * @return
	 * @see org.apache.commons.logging.Log#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#trace(java.lang.Object)
	 */
	public void trace(Object message) {
		log.trace(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#trace(java.lang.Object, java.lang.Throwable)
	 */
	public void trace(Object message, Throwable t) {
		log.trace(logInfo + message, t);
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#debug(java.lang.Object)
	 */
	public void debug(Object message) {
		log.debug(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
	 */
	public void debug(Object message, Throwable t) {
		log.debug(logInfo + message, t);
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#info(java.lang.Object)
	 */
	public void info(Object message) {
		log.info(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#info(java.lang.Object, java.lang.Throwable)
	 */
	public void info(Object message, Throwable t) {
		log.info(logInfo + message, t);
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#warn(java.lang.Object)
	 */
	public void warn(Object message) {
		log.warn(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#warn(java.lang.Object, java.lang.Throwable)
	 */
	public void warn(Object message, Throwable t) {
		log.warn(logInfo + message, t);
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#error(java.lang.Object)
	 */
	public void error(Object message) {
		log.error(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
	 */
	public void error(Object message, Throwable t) {
		log.error(logInfo + message, t);
	}

	/**
	 * @param message
	 * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
	 */
	public void fatal(Object message) {
		log.fatal(logInfo + message);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
	 */
	public void fatal(Object message, Throwable t) {
		log.fatal(logInfo + message, t);
	}
	

	

}
