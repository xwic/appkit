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
package de.xwic.appkit.webbase.async;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;

/**
 * This class is used to properly close the dialog when the backend process
 * was completed.
 * 
 * @author lippisch
 */
@JavaScriptSupport
public class AsyncProcessControlHelper extends Control {

	private Control toBeDestroyed;
	private boolean destroyNow = false;

	/**
	 * @param container
	 * @param name
	 */
	public AsyncProcessControlHelper(IControlContainer container, String name, Control toBeDestroyed) {
		super(container, name);
		this.toBeDestroyed = toBeDestroyed;
	}

	/**
	 * @return the destroyNow
	 */
	public boolean isDestroyNow() {
		return destroyNow;
	}
	

	/**
	 * Invoked by the JavaScript if the dialog is ready to be closed.
	 */
	public void actionCloseDialog() {
		if (toBeDestroyed != null) {
			toBeDestroyed.destroy();
		}
	}

	/**
	 * @param destroyNow the destroyNow to set
	 */
	public void setDestroyNow(boolean destroyNow) {
		this.destroyNow = destroyNow;
		requireRedraw();
	}
	
}
