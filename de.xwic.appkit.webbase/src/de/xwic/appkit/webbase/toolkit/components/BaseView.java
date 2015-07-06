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
package de.xwic.appkit.webbase.toolkit.components;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.ToolBarSpacer;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DataAccessException;

/**
 * 
 * The main content views should extend this class. This is
 * a frame for the content.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class BaseView extends ControlContainer implements IOuterLayout {

    private String title = "";
    private boolean isBottomView = false;
    private boolean isLeftView = false;
    /** the action bar */
    protected ToolBar toolbar;
    /** the error warning control*/
    protected ErrorWarning errorWarning;

    /**
	 * The main content views should extend this class. This is
	 * a frame for the content.
     * 
     * @param container
     * @param name
     */
    public BaseView(IControlContainer container, String name) {
    	super(container, name);
        setRendererId(DEFAULT_OUTER_RENDERER);
        toolbar = new ToolBar(this, "toolbar");
        errorWarning = new ErrorWarning(this, "errorWarning");
        errorWarning.setAutoClose(true);
        errorWarning.setShowStackTrace(false);
    }


    /**
     * Show the ErrorWarningControl as error with red background
     * 
     * @param message
     */
    public void showError(String message) {
        errorWarning.showError(message);
        errorWarning.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Show the ErrorWarningControl as warning with yellow background or specified via css.
     * 
     * @param message
     * @param cssClass
     */
    public void showWarning(String message, String cssClass) {
        errorWarning.showWarning(message, cssClass);
        errorWarning.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Show a warning message.
     * 
     * @param message
     * @param cssClass
     */
    public void showWarning(String message) {
        getSessionContext().notifyMessage(message, "xwic-notify-warning");
    }
	
    /**
     * Show a warning message.
     * 
     * @param message
     * @param cssClass
     */
    public void showInfo(String message) {
        getSessionContext().notifyMessage(message, "xwic-notify-info");
    }

    /**
     *  Set this to true, if the message should disappear after redraw.
     *  False will show the message as long as it will get closed manually.
     *  
     * @param doClose
     */
    public void closeErrorMessageOnRedraw(boolean doClose) {
        errorWarning.setAutoClose(doClose);
    }


    /**
     * Show the ErrorWarningControl with an exception.
     * 
     * @param e
     */
    public void showError(Exception e) {
        String langID = getSessionContext().getLocale().getLanguage();
        if (e instanceof DataAccessException) {
            DataAccessException dae = (DataAccessException)e;
            errorWarning.showError(createDataAccessExceptionContent(dae, langID));
        } else if (e instanceof DeleteFailedException) {
        	errorWarning.showError(e);
        } else {
            log.error("ShowError: ", e);
            errorWarning.showError(e);
        }
        errorWarning.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Tries to format the content of the given DataAccessException.
     * 
     * @param dae
     * @param langID
     * @return a string for the error control.
     */
    private String createDataAccessExceptionContent(DataAccessException dae, String langID) {
        StringBuilder builder = new StringBuilder();
        Bundle coreBundle = null;
        try {
            coreBundle = ConfigurationManager.getSetup().getDomain("core").getBundle(langID);
        }
        catch (Exception ex) {
            builder.append(ex.getMessage());
            builder.append("<br>");
        }

        if (coreBundle == null) {
            builder.append("No Common Bundle found.");
            builder.append("<br>");
            builder.append("Common Error Strings cannot be resolved, showing keys only.");
            builder.append("<br>");
        }

        if (dae.getMessage() == "softdelete.hasref") {
            List<?> errorContent = (List<?>)dae.getInfo();

            //dirtydirtydirty... if the customer likes it, replace strings
            //if not, delete the code below :(
            if (coreBundle != null) {
                builder.append(coreBundle.getString("softdel.hasref"));
            }
            else {
                builder.append(dae.getMessage());
            }

            builder.append("<br><br><table class=\"datalist\" cellpadding=\"2\" cellspacing=\"0\"><tr><th>Entity ID</th><th>Entity Type</th><th>Description</th><th>Used Property</th></tr>");

            int maxCounter = 5;
            int i = 0;
            boolean hasmore = false;
            
            for (Object obj : errorContent) {
            	String stringEntry = (String) obj;
                if (i == maxCounter) {
                    builder.append("</table><br>");
                    builder.append("&nbsp;&nbsp;+ ").append(errorContent.size() - maxCounter).append(" Weitere... ");
                    hasmore = true;
                    break;
                }
                builder.append("<tr>");
                StringTokenizer stk = new StringTokenizer(stringEntry, ";");
                String type = stk.nextToken();
                String propName = stk.nextToken();
                String id = stk.nextToken();
                String propTitle = null;
                String entityTitle = null;
                try {
                    EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(type);
                    Bundle bundle = ed.getDomain().getBundle(langID);
                    propTitle = bundle.getString(type + "." + propName);
                    entityTitle = bundle.getString(type);

                    if (propTitle.startsWith("!")) {
                        propTitle = propName;
                    }
                    if (entityTitle.startsWith("!")) {
                        entityTitle = type.substring(type.lastIndexOf(".") + 1);
                    }
                }
                catch (ConfigurationException ce) {
                    builder.append(ce.getMessage());
                    propTitle = propName;
                    entityTitle = type.substring(type.lastIndexOf(".") + 1);
                }

                builder.append("<td>");
                builder.append(id);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(type);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(entityTitle);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(propTitle);
                builder.append("</td>");
                builder.append("</tr>");

                i++;
            }
            if (!hasmore) {
                builder.append("</table><br>");
            }
            log.error("ShowError: " + builder, dae);

        }
        else {
            builder.append(dae.getMessage());
        }

        return builder.toString();
    }

    

    /**
     * @return the title of the view.
     */
    public String getTitle() {
		return title;
	}

    /**
     * Sets the title for the view.
     * 
     * @param title
     */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return true, if view is bottom view.
	 */
	public boolean isBottomView() {
		return isBottomView;
	}

	/**
	 * Sets flag, if view is a bottom displayed view.
	 * 
	 * @param isBottomView
	 */
	public void setBottomView(boolean isBottomView) {
		this.isBottomView = isBottomView;
	}

	/**
	 * @return true, if view is left view
	 */
	public boolean isLeftView() {
		return isLeftView;
	}

	/**
	 * Sets flag, if view is a left oriented view.
	 * 
	 * @param isLeftView
	 */
	public void setLeftView(boolean isLeftView) {
		this.isLeftView = isLeftView;
	}


	/**
	 * @return true if the actionBar contains actions.
	 */
    public boolean hasActions() {
		if (!toolbar.isVisible()) {
    		return false;
    	}
    	
		for (Iterator<Control> it = toolbar.getControls(); it.hasNext();) {
			Control c = it.next();
			if (c instanceof ToolBarGroup) {
				if (((ToolBarGroup) c).getControls().hasNext()) {
					// if it has any controls, return true
					return true;
				}
			} else if (!(c instanceof ToolBarSpacer)) {
				return true;
			}
		}
        
    	return false;
    }
    
    @Override
    public String getTemplateName() {
        if (super.getTemplateName().equals(getOuterTemplateName())) {
            return null;
        }
        return super.getTemplateName();
    }

    /*
     * (non-Javadoc)
     * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
     */
    public String getOuterTemplateName() {
        return BaseView.class.getName();
    }


	/**
	 * @return the actionBar
	 */
	public ToolBar getToolbar() {
		return toolbar;
	}

}
