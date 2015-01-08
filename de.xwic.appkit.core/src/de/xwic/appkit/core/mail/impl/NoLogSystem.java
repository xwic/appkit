/*
 * Copyright 2005-2007 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.renderer.velocity.NoLogSystem
 * Created on Mar 5, 2013
 * $Id:$
 */
package de.xwic.appkit.core.mail.impl;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

/**
 * This log system completely swallows all velocity messages. This may be used on a production environment to prevent logging due to
 * performance reasons.
 * 
 * @author lippisch
 */
public class NoLogSystem implements LogSystem {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
	 */
	public void init(RuntimeServices arg0) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int, java.lang.String)
	 */
	public void logVelocityMessage(int arg0, String arg1) {

	}

}
