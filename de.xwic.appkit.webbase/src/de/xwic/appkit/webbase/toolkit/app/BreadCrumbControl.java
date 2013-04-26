/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Window;
import de.jwic.controls.Window;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;

/**
 * Control for the navigation path. 
 * 
 * @author Ronny Pfretzschner
 *
 */
public class BreadCrumbControl extends Control implements IStackChangedListener {
    
	private StackedContainerWithEvent controlStack;

	/**
	 * Creates the control for the navigation path.
	 * 
	 * @param parent
	 * @param name
	 */
    public BreadCrumbControl(IControlContainer parent, String name) {
    	super(parent, name);
        
    }

    /**
     * @return a list with the names of the control stack.
     */
    public List<String> getBreadCrumbs() {
        ArrayList<String> list = new ArrayList<String>();
        Iterator i = controlStack.getControls();
        while (i.hasNext()) {
        	Control con = (Control) i.next();
        	String key = con.getName();
 //           if (!key.equals(controlStack.getCurrentControlName())) {
                list.add(key);
//            }
        }
        return list;
    }

    /**
     * @return title of this control displayed on ui
     */
    public String getPathTitle() {
        return BundleAccessor.getDomainBundle(this, ExtendedApplication.CORE_DOMAIN_ID).getString("breadcrumb.path");
    }

    /**
     * Returns the name of the control by the given key.
     * 
     * @param key
     * @return
     */
    public String getCrumbTitle(String key) {
        Control control = controlStack.getControl(key);
        if (control == null) {
            return "[" + key + "]";
        } else {
            return getControlTitle(control);
        }
    }

    /**
     * Tries to get the name of the given control.
     * 
     * @param control
     * @return
     */
    private String getControlTitle(Control control) {

        if (control instanceof IPageControl) {
            IPageControl page = (IPageControl) control;
            return page.getTitle();
        } else if (control instanceof Window) {
            Window win = (Window) control;
            return win.getTitle();
        } else if (control instanceof ControlContainer) {
            // controlContainer has no title -> look for usefull content...
            ControlContainer container = (ControlContainer) control;
            Iterator i = container.getControls();
            
            if (i.hasNext()) {
                return getControlTitle((Control) i.next());
            }
            return "[EC:" + control.getName() + "]";
        } 
        return "[" + control.getClass().getName() + ", " + control.getName() + "?]";

    }

    /**
     * Called, when the user pressed a displayed control in the path.
     * 
     * @param key
     */
    public void actionNavigate(String key) {
        if (key != null && !key.equals(controlStack.getCurrentControlName()) && controlStack.getControl(key) != null) {
            // pop the controls until we reach the key
        	
        	int i = 0;
            while (!key.equals(controlStack.getCurrentControlName()) && controlStack.getControl(controlStack.getCurrentControlName()) != null) {
                controlStack.removeControl(controlStack.getCurrentControlName());
                if (i++ > 100) {
                	log.error("breaking out of actionNavigate as stack seems to long!", new Exception());
                	break;
                }
            }
        }
    }

    /**
     * The stack containing all registered controls.
     * 
     * @return StackedContainerWithEvent
     */
    public StackedContainerWithEvent getControlStack() {
        return controlStack; 
    }
    
    /**
     * Sets the control stack, where all viewing controls will be registered.
     *  
     * @param value
     */
    public void setControlStack(StackedContainerWithEvent value) {
        if (controlStack != null) {
        	controlStack.removeStackChangedListener(this);
        } 
        controlStack = value;
        controlStack.addStackChangedListener(this);
    }

    /*
     * (non-Javadoc)
     * @see de.pol.netapp.spc.toolkit.app.IStackChangedListener#stackChanged()
     */
	public void stackChanged() {
		setRequireRedraw(true);
	}
}
