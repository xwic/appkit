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
package de.xwic.appkit.core.remote.client;


/**
 * Interface to define remote function call. Client and server need to have
 * access to this implementation. Currently it's only implemented for static 
 * functions. The method which should be called needs to have following structure:
 * Object functionName(IRemoteFunctionCallConditions conditions);
 * @author dotto
 *
 */
public abstract interface IRemoteFunctionCallConditions {
	
	/**
	 * Classname which contains the method to be called
	 * @return
	 */
	String className();
	
	/**
	 * Name of the function to be called.
	 * @return
	 */
	String functionName();
}
