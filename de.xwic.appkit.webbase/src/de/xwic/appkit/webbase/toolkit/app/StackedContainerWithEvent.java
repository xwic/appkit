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
package de.xwic.appkit.webbase.toolkit.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.JWicException;
import de.jwic.base.JavaScriptSupport;

/**
 * Same implementation as the StackedContainer. But throws
 * an event, when the stack was changed.
 * 
 * @author Ronny Pfretzschner
 *
 */
@JavaScriptSupport
public class StackedContainerWithEvent extends ControlContainer {

    private List<IStackChangedListener> changeListeners = new ArrayList<IStackChangedListener>();

	private String currentControlName = null;
	private Stack<String> stack = new Stack<String>();
	private String widthHint = "";
	private String heightHint = "";

	/**
	 * Creates the same implementation as the StackedContainer. But throws
	 * an event, when the stack was changed.
	 * 
	 * @param container
	 * @param name
	 */
	public StackedContainerWithEvent(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * Creates the same implementation as the StackedContainer. But throws
	 * an event, when the stack was changed.
	 * 
	 * @param container
	 */
	public StackedContainerWithEvent(IControlContainer container) {
		super(container);
	}

    
	/**
	 * Adds the given listener to the list.
	 *  
	 * @param listener
	 */
    public void addStackChangedListener(IStackChangedListener listener) {
    	if (!changeListeners.contains(listener)) {
    		changeListeners.add(listener);
    	}
    }
    
    /**
     * Removes the given listener from the list.
     * 
     * @param listener
     */
    public void removeStackChangedListener(IStackChangedListener listener) {
    	if (changeListeners.contains(listener)) {
    		changeListeners.remove(listener);
    	}
    }
    
    /**
     * Informs the registered listeners.
     */
    @SuppressWarnings("unchecked")
	private void stackChanged() {
    	List<IStackChangedListener> clone = (List<IStackChangedListener>)((ArrayList<IStackChangedListener>) changeListeners).clone();
    	
    	for (IStackChangedListener eventListener : clone) {
			eventListener.stackChanged();
		}
    }
    
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#registerControl(de.jwic.base.Control, java.lang.String)
	 */
	public void registerControl(Control control, String name) throws JWicException {
		super.registerControl(control, name);
		stack.push(control.getName());
		stackChanged();
		if (currentControlName == null) {
			currentControlName = control.getName();
			requireRedraw();
		}
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#unregisterControl(de.jwic.base.Control)
	 */
	public void unregisterControl(Control control) {
		stack.remove(control.getName());
		stackChanged();
		if (currentControlName.equals(control.getName())) {
			if (!stack.isEmpty()) {
				currentControlName = (String)stack.peek();
			} else {
				currentControlName = null;
			}
		}
		super.unregisterControl(control);
	}
	
	/**
	 * @return Returns the currentControlName.
	 */
	public String getCurrentControlName() {
		return currentControlName;
	}

	/**
	 * @param currentControlName The currentControlName to set.
	 */
	public void setCurrentControlName(String currentControlName) {
		this.currentControlName = currentControlName;
		requireRedraw();
	}
	
	/**
	 * @return Returns the heightHint.
	 */
	public String getHeightHint() {
		return heightHint;
	}

	/**
	 * @param heightHint The heightHint to set.
	 */
	public void setHeightHint(String heightHint) {
		this.heightHint = heightHint;
	}

	/**
	 * @return Returns the widthHint.
	 */
	public String getWidthHint() {
		return widthHint;
	}

	/**
	 * @param widthHint The widthHint to set.
	 */
	public void setWidthHint(String widthHint) {
		this.widthHint = widthHint;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#isRenderingRelevant(de.jwic.base.Control)
	 */
	public boolean isRenderingRelevant(Control childControl) {
		return childControl.getName().equals(currentControlName);
	}

	/**
	 * @return the scrollTop
	 */
	public int getScrollTop() {
		if (currentControlName != null) {
			Control control = getControl(currentControlName);
			if (control instanceof InnerPage) {
				InnerPage ipage = (InnerPage)control;
				return ipage.getScrollTop();
			}
			return 0;
		}
		return -1;
	}

	/**
	 * @return the scrollTop
	 */
	public int getScrollLeft() {
		if (currentControlName != null) {
			Control control = getControl(currentControlName);
			if (control instanceof InnerPage) {
				InnerPage ipage = (InnerPage)control;
				return ipage.getScrollLeft();
			}
			return 0;
		}
		return -1;
	}

	
}
