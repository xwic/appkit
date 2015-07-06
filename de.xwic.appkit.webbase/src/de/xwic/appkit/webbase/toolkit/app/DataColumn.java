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
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * @author bogdan
 *
 */
public class DataColumn extends ControlContainer implements IOuterLayout{

	/**
	 * @param container
	 */
	public DataColumn(IControlContainer container) {
		super(container);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/**
	 * @param container
	 * @param name
	 */
	public DataColumn(IControlContainer container, String name) {
		super(container, name);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName()+"_outerLayout";
	}
	
	/**
	 * @return new DataBlock
	 */
	public DataBlock addDataBlock(){
		return new DataBlock(this);
	}
	
	/**
	 * @param name of DataBlock 
	 * @return new DataBlock with given name
	 */
	public DataBlock addDataBlock(String name){
		return new DataBlock(this,name);
	}

	
	
}
